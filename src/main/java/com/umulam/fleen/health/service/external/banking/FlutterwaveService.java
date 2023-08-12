package com.umulam.fleen.health.service.external.banking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umulam.fleen.health.adapter.banking.flutterwave.FlutterwaveAdapter;
import com.umulam.fleen.health.adapter.banking.flutterwave.model.enums.FwBankCountryType;
import com.umulam.fleen.health.adapter.banking.flutterwave.model.request.FwGetExchangeRateRequest;
import com.umulam.fleen.health.adapter.banking.flutterwave.model.response.FwGetBanksResponse;
import com.umulam.fleen.health.adapter.banking.flutterwave.model.response.FwGetExchangeRateResponse;
import com.umulam.fleen.health.repository.jpa.BankAccountJpaRepository;
import com.umulam.fleen.health.service.BankingService;
import com.umulam.fleen.health.service.MemberService;
import com.umulam.fleen.health.service.impl.BankingServiceImpl;
import com.umulam.fleen.health.service.impl.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.umulam.fleen.health.constant.base.GeneralConstant.FLUTTERWAVE_GET_BANKS_CACHE_PREFIX;
import static java.util.Objects.isNull;

@Slf4j
@Service
@Primary
public class FlutterwaveService extends BankingServiceImpl implements BankingService {

  private final FlutterwaveAdapter flutterwaveAdapter;
  private final CacheService cacheService;
  private final BankAccountJpaRepository bankAccountJpaRepository;
  private final MemberService memberService;
  private final ObjectMapper mapper;

  public FlutterwaveService(FlutterwaveAdapter flutterwaveAdapter,
                         CacheService cacheService,
                         BankAccountJpaRepository bankAccountJpaRepository,
                         MemberService memberService,
                         ObjectMapper mapper) {
    super(mapper);
    this.flutterwaveAdapter = flutterwaveAdapter;
    this.cacheService = cacheService;
    this.bankAccountJpaRepository = bankAccountJpaRepository;
    this.memberService = memberService;
    this.mapper = mapper;
  }

  public List<FwGetBanksResponse.FwBankData> getBanks(String country) {
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

  @Override
  public String getTransactionStatusByReference(String transactionReference) {
    return flutterwaveAdapter.verifyTransactionByReference(transactionReference).getData().getStatus();
  }

}
