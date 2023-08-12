package com.umulam.fleen.health.service.external.banking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umulam.fleen.health.adapter.banking.flutterwave.FlutterwaveAdapter;
import com.umulam.fleen.health.adapter.banking.flutterwave.model.enums.FwBankCountryType;
import com.umulam.fleen.health.adapter.banking.flutterwave.model.request.FwCreateTransferRequest;
import com.umulam.fleen.health.adapter.banking.flutterwave.model.request.FwGetExchangeRateRequest;
import com.umulam.fleen.health.adapter.banking.flutterwave.model.request.FwGetTransferFeeRequest;
import com.umulam.fleen.health.adapter.banking.flutterwave.model.request.FwResolveBankAccountRequest;
import com.umulam.fleen.health.adapter.banking.flutterwave.model.response.FwGetBanksResponse;
import com.umulam.fleen.health.adapter.banking.flutterwave.model.response.FwGetExchangeRateResponse;
import com.umulam.fleen.health.adapter.banking.flutterwave.model.response.FwGetTransferFeeResponse;
import com.umulam.fleen.health.adapter.banking.flutterwave.model.response.FwResolveBankAccountResponse;
import com.umulam.fleen.health.constant.session.*;
import com.umulam.fleen.health.exception.banking.BankAccountNotFoundException;
import com.umulam.fleen.health.exception.banking.EarningsAccountNotFoundException;
import com.umulam.fleen.health.exception.banking.InsufficientEarningsBalance;
import com.umulam.fleen.health.exception.banking.WithdrawalAmountGreaterThanEarningsBalanceException;
import com.umulam.fleen.health.model.domain.Earnings;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.MemberBankAccount;
import com.umulam.fleen.health.model.domain.transaction.WithdrawalTransaction;
import com.umulam.fleen.health.model.dto.banking.AddBankAccountDto;
import com.umulam.fleen.health.model.dto.banking.CreateWithdrawalDto;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.repository.jpa.BankAccountJpaRepository;
import com.umulam.fleen.health.repository.jpa.EarningsJpaRepository;
import com.umulam.fleen.health.repository.jpa.transaction.WithdrawalTransactionJpaRepository;
import com.umulam.fleen.health.service.BankingService;
import com.umulam.fleen.health.service.MemberService;
import com.umulam.fleen.health.service.impl.BankingServiceImpl;
import com.umulam.fleen.health.service.impl.CacheService;
import com.umulam.fleen.health.service.impl.ConfigService;
import com.umulam.fleen.health.util.FleenHealthReferenceGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.umulam.fleen.health.adapter.banking.flutterwave.model.request.FwCreateTransferRequest.CreateTransferMetadata;
import static com.umulam.fleen.health.adapter.banking.flutterwave.model.response.FwGetBanksResponse.FwBankData;
import static com.umulam.fleen.health.constant.base.FleenHealthConstant.TRANSFER_NARRATION_OR_DESCRIPTION;
import static com.umulam.fleen.health.constant.base.GeneralConstant.FLUTTERWAVE_GET_BANKS_CACHE_PREFIX;
import static com.umulam.fleen.health.util.StringUtil.getFullName;
import static java.util.Objects.isNull;

@Slf4j
@Service
@Qualifier("flutterwaveService")
public class FlutterwaveService extends BankingServiceImpl implements BankingService {

  private final FlutterwaveAdapter flutterwaveAdapter;
  private final CacheService cacheService;
  private final BankAccountJpaRepository bankAccountJpaRepository;
  private final EarningsJpaRepository earningsJpaRepository;
  private final FleenHealthReferenceGenerator referenceGenerator;
  private final WithdrawalTransactionJpaRepository withdrawalTransactionJpaRepository;
  private final ConfigService configService;
  private final MemberService memberService;
  private final ObjectMapper mapper;

  public FlutterwaveService(FlutterwaveAdapter flutterwaveAdapter,
                         CacheService cacheService,
                         BankAccountJpaRepository bankAccountJpaRepository,
                         MemberService memberService,
                         ObjectMapper mapper,
                         EarningsJpaRepository earningsJpaRepository,
                         FleenHealthReferenceGenerator referenceGenerator,
                         ConfigService configService,
                         WithdrawalTransactionJpaRepository withdrawalTransactionJpaRepository) {
    super(bankAccountJpaRepository, mapper, earningsJpaRepository);
    this.flutterwaveAdapter = flutterwaveAdapter;
    this.cacheService = cacheService;
    this.bankAccountJpaRepository = bankAccountJpaRepository;
    this.memberService = memberService;
    this.referenceGenerator = referenceGenerator;
    this.mapper = mapper;
    this.earningsJpaRepository = earningsJpaRepository;
    this.configService = configService;
    this.withdrawalTransactionJpaRepository = withdrawalTransactionJpaRepository;
  }

  public List<FwBankData> getBanks(String country) {
    String cacheKey = getBanksCacheKey().concat(country.toUpperCase());
    if (cacheService.exists(cacheKey)) {
      return cacheService.get(cacheKey, FwGetBanksResponse.class).getData();
    }

    FwGetBanksResponse banksResponse = flutterwaveAdapter.getBanks(country);
    cacheService.set(cacheKey, banksResponse);
    return banksResponse.getData();
  }

  @EventListener(ApplicationReadyEvent.class)
  public void saveBanksToCacheOnStartup() {
    FwGetBanksResponse banksResponse = flutterwaveAdapter.getBanks(FwBankCountryType.NG.getValue());
    saveBanksFwToCache(banksResponse, null);
  }

  @Scheduled(cron = "0 0 */12 * * *")
  private void saveBanksToCache() {
    saveBanksFwToCache(null, FwBankCountryType.NG.getValue());
  }

  private void saveBanksFwToCache(FwGetBanksResponse response, String country) {
    if (isNull(response) || isNull(response.getData()) || response.getData().isEmpty()) {
      saveBanksToCacheOnStartup();
    }
    String key = getBanksCacheKey().concat(Objects.toString(country, FwBankCountryType.NG.getValue()));
    cacheService.set(key, response);
  }

  private String getBanksCacheKey() {
    return FLUTTERWAVE_GET_BANKS_CACHE_PREFIX;
  }

  public FwGetExchangeRateResponse getExchangeRate(Double amount, String sourceCurrency, String destinationCurrency) {
    FwGetExchangeRateRequest request = FwGetExchangeRateRequest.builder()
      .amount(amount.toString())
      .sourceCurrency(sourceCurrency)
      .destinationCurrency(destinationCurrency)
      .build();

    return flutterwaveAdapter.getExchangeRate(request);
  }

  @Transactional
  public void addBankAccount(AddBankAccountDto dto, FleenUser user) {
    FwResolveBankAccountRequest request = FwResolveBankAccountRequest.builder()
      .accountNumber(dto.getAccountNumber())
      .bankCode(dto.getBankCode())
      .build();

    String recipientType = dto.getRecipientType().toUpperCase();
    String currency = dto.getCurrency().toUpperCase();
    CurrencyType currencyType = CurrencyType.valueOf(currency);
    List<FwBankData> banks = getBanks(currencyType.getCountry());

    checkAccountDetails(dto, currency, recipientType);
    FwResolveBankAccountResponse bankAccountResponse = flutterwaveAdapter.resolveBankAccount(request);

    FwBankData bankDetails = banks
      .stream()
      .filter(bank -> Objects.equals(bank.getCode(), dto.getBankCode()))
      .findFirst()
      .orElse(null);

    MemberBankAccount bankAccount = dto.toBankAccount();
    bankAccount.setBankCode(dto.getBankCode());
    bankAccount.setAccountName(bankAccountResponse.getData().getAccountName());
    bankAccount.setBankName(Objects.nonNull(bankDetails) ? bankDetails.getName(): "UNKNOWN");
    bankAccount.setMember(Member.builder().id(user.getId()).build());
    bankAccountJpaRepository.save(bankAccount);
  }

  @Override
  public String getTransactionStatusByReference(String transactionReference) {
    return flutterwaveAdapter.verifyTransactionByReference(transactionReference).getData().getStatus();
  }

  @Override
  public boolean isBankCodeExists(String bankCode, String country) {
    List<FwBankData> banks = getBanks(country);
    return banks
      .stream()
      .anyMatch(bank -> bank.getCode().equalsIgnoreCase(bankCode));
  }

  @Override
  @Transactional
  public void createWithdrawal(CreateWithdrawalDto dto, FleenUser user) {
    Optional<MemberBankAccount> bankAccountExists = bankAccountJpaRepository.findById(Integer.parseInt(dto.getBankAccount()));
    if (bankAccountExists.isEmpty()) {
      throw new BankAccountNotFoundException(dto.getBankAccount());
    }

    MemberBankAccount bankAccount = bankAccountExists.get();
    Optional<Earnings> earningsExists = earningsJpaRepository.findByMember(user.toMember());
    if (earningsExists.isEmpty()) {
      throw new EarningsAccountNotFoundException();
    }

    Earnings earnings = earningsExists.get();
    int canWithdraw = dto.getAmount().compareTo(earnings.getTotalEarnings());
    if (canWithdraw < 0) {
      throw new InsufficientEarningsBalance(dto.getAmount().doubleValue(), earnings.getTotalEarnings().doubleValue());
    }

    FwGetTransferFeeRequest request = FwGetTransferFeeRequest.builder()
      .amount(dto.getAmount().toString())
      .currency(bankAccount.getCurrency())
      .build();
    request.setTransferType(bankAccount);

    FwGetTransferFeeResponse transferFeeResponse = flutterwaveAdapter.getTransferFee(request);
    double transferFee = transferFeeResponse.getData().get(0).getFee();
    double balance = earnings.getTotalEarnings().doubleValue() - dto.getAmount().doubleValue();
    double amountToTransfer;

    if (configService.getPaymentIssuingCurrency().equalsIgnoreCase(bankAccount.getCurrency())) {
      amountToTransfer = dto.getAmount().doubleValue() - transferFee;
      if (amountToTransfer < 0) {
        throw new WithdrawalAmountGreaterThanEarningsBalanceException(dto.getAmount().doubleValue(), transferFee);
      }
    }
    else {
      FwGetExchangeRateResponse exchangeRate = getExchangeRate(dto.getAmount().doubleValue(), configService.getPaymentIssuingCurrency(), bankAccount.getCurrency());
      amountToTransfer = exchangeRate.getData().getSource().getAmount() - transferFee;
    }

    Member member = earnings.getMember();
    FwCreateTransferRequest transferRequest = FwCreateTransferRequest.builder()
      .amount(amountToTransfer)
      .bankCode(bankAccount.getBankCode())
      .accountNumber(bankAccount.getAccountNumber())
      .description(TRANSFER_NARRATION_OR_DESCRIPTION)
      .sourceCurrency(configService.getPaymentIssuingCurrency())
      .destinationCurrency(bankAccount.getCurrency())
      .transactionReference(referenceGenerator.generateTransactionReference())
      .beneficiaryName(getFullName(member.getFirstName(), member.getLastName()))
      .build();

    CreateTransferMetadata transferMetadata = CreateTransferMetadata.builder()
      .firstName(member.getFirstName())
      .lastName(member.getLastName())
      .countryCode(CurrencyType.valueOf(bankAccount.getCurrency()).getCountry())
      .senderCountryCode(CurrencyType.NGN.getValue())
      .issuer(configService.getPaymentIssuer())
      .merchantName(configService.getPaymentIssuer())
      .mobileNumber(member.getPhoneNumber())
      .emailAddress(member.getEmailAddress())
      .recipientAddress(member.getAddress())
      .build();
    transferRequest.setMeta(transferMetadata);

    WithdrawalTransaction transaction = WithdrawalTransaction.builder()
      .recipient(member)
      .withdrawalStatus(WithdrawalStatus.PENDING)
      .status(TransactionStatus.PENDING)
      .gateway(PaymentGateway.FLUTTERWAVE)
      .type(TransactionType.WITHDRAWAL)
      .subType(TransactionSubType.DEBIT)
      .reference(referenceGenerator.generateTransactionReference())
      .currency(configService.getPaymentIssuingCurrency())
      .accountName(bankAccount.getAccountName())
      .accountNumber(bankAccount.getAccountNumber())
      .bankName(bankAccount.getBankName())
      .bankCode(bankAccount.getBankCode())
      .amount(dto.getAmount().doubleValue())
      .build();
    withdrawalTransactionJpaRepository.save(transaction);

    earnings.setTotalEarnings(new BigDecimal(balance));
    earningsJpaRepository.save(earnings);
    flutterwaveAdapter.createTransfer(transferRequest);
  }

}
