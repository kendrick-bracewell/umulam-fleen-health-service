package com.umulam.fleen.health.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umulam.fleen.health.adapter.banking.model.PaymentRecipientType;
import com.umulam.fleen.health.constant.authentication.PaymentGatewayType;
import com.umulam.fleen.health.constant.session.CurrencyType;
import com.umulam.fleen.health.exception.banking.BankAccountAlreadyExists;
import com.umulam.fleen.health.exception.banking.BankAccountNotFoundException;
import com.umulam.fleen.health.exception.banking.InvalidAccountTypeCombinationException;
import com.umulam.fleen.health.exception.banking.InvalidBankCodeException;
import com.umulam.fleen.health.exception.base.FleenHealthException;
import com.umulam.fleen.health.model.domain.Earnings;
import com.umulam.fleen.health.model.domain.MemberBankAccount;
import com.umulam.fleen.health.model.dto.banking.AddBankAccountDto;
import com.umulam.fleen.health.model.dto.banking.CreateWithdrawalDto;
import com.umulam.fleen.health.model.event.InternalPaymentValidation;
import com.umulam.fleen.health.model.event.flutterwave.FwChargeEvent;
import com.umulam.fleen.health.model.event.paystack.PsChargeEvent;
import com.umulam.fleen.health.model.response.SupportedCountry;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.repository.jpa.BankAccountJpaRepository;
import com.umulam.fleen.health.repository.jpa.EarningsJpaRepository;
import com.umulam.fleen.health.service.BankingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@Primary
public class BankingServiceImpl implements BankingService {

  private final BankAccountJpaRepository bankAccountJpaRepository;
  private final EarningsJpaRepository earningsJpaRepository;
  private final ObjectMapper mapper;

  public BankingServiceImpl(BankAccountJpaRepository bankAccountJpaRepository,
                            ObjectMapper mapper,
                            EarningsJpaRepository earningsJpaRepository) {
    this.bankAccountJpaRepository = bankAccountJpaRepository;
    this.mapper = mapper;
    this.earningsJpaRepository = earningsJpaRepository;
  }

  @Override
  public String getTransactionStatusByReference(String transactionReference) {
    return null;
  }

  public InternalPaymentValidation getInternalPaymentValidationByChargeEvent(String body, PaymentGatewayType paymentGatewayType) {
    try {
      if (paymentGatewayType == PaymentGatewayType.FLUTTERWAVE) {
        FwChargeEvent event = mapper.readValue(body, FwChargeEvent.class);
        return InternalPaymentValidation.builder()
          .status(event.getData().getStatus())
          .transactionReference(event.getData().getTransactionReference())
          .externalSystemTransactionReference(event.getData().getExternalSystemReference())
          .currency(event.getData().getCurrency())
          .build();
      } else if (paymentGatewayType == PaymentGatewayType.PAYSTACK) {
        PsChargeEvent event = mapper.readValue(body, PsChargeEvent.class);
        return InternalPaymentValidation.builder()
          .status(event.getData().getStatus())
          .transactionReference(event.getData().getReference())
          .externalSystemTransactionReference(event.getData().getMetadata().getTransactionReference())
          .currency(event.getData().getCurrency())
          .build();
      }
    } catch (JsonProcessingException ex) {
      log.error(ex.getMessage(), ex);
    }
    return null;
  }

  @Override
  public List<SupportedCountry> getSupportedCountries() {
    List<SupportedCountry> supportedCountries = new ArrayList<>();
    for (CurrencyType currencyType : CurrencyType.values()) {
      supportedCountries.add(SupportedCountry.builder().currency(null).build());
    }
    return null;
  }

  @Override
  public boolean isBankCodeExists(String bankCode, String countryOrCurrency) {
    return false;
  }

  public static boolean isAccountTypeCombinationValid(String recipientType, String currencyType) {
    PaymentRecipientType recipient = PaymentRecipientType.valueOf(recipientType.toUpperCase());
    CurrencyType currency = CurrencyType.valueOf(currencyType.toUpperCase());

    switch (recipient) {
      case NUBAN:
        return currency == CurrencyType.NGN;
      case MOBILE_MONEY:
        return currency == CurrencyType.GHS;
      case BASA:
        return currency == CurrencyType.ZAR;
      case FLUTTERWAVE:
        return true;
      default:
        return false;
    }
  }

  public void checkAccountDetails(AddBankAccountDto dto, String currency, String recipientType) {
    if (!isBankCodeExists(dto.getBankCode(), currency)) {
      throw new InvalidBankCodeException(dto.getBankCode());
    }

    if (!isAccountTypeCombinationValid(recipientType, currency)) {
      throw new InvalidAccountTypeCombinationException(recipientType, currency);
    }

    boolean accountNumberExist = bankAccountJpaRepository.existsByAccountNumber(dto.getAccountNumber());
    if (accountNumberExist) {
      throw new BankAccountAlreadyExists(dto.getAccountNumber());
    }
  }

  @Override
  public void createWithdrawal(CreateWithdrawalDto dto, FleenUser user) {
    Optional<MemberBankAccount> bankAccountExists = bankAccountJpaRepository.findById(Integer.parseInt(dto.getBankAccount()));
    if (bankAccountExists.isEmpty()) {
      throw new BankAccountNotFoundException(dto.getBankAccount());
    }

    MemberBankAccount bankAccount = bankAccountExists.get();
    Optional<Earnings> earningsExists = earningsJpaRepository.findByMember(user.toMember());
    if (earningsExists.isEmpty()) {
      throw new FleenHealthException("Earnings not found");
    }

    Earnings earnings = earningsExists.get();
    int canWithdraw = dto.getAmount().compareTo(earnings.getTotalEarnings());
    if (canWithdraw < 0) {
      throw new FleenHealthException("Insufficient balance");
    }


  }
}
