package com.umulam.fleen.health.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umulam.fleen.health.adapter.banking.model.PaymentRecipientType;
import com.umulam.fleen.health.constant.authentication.PaymentGatewayType;
import com.umulam.fleen.health.constant.session.CurrencyType;
import com.umulam.fleen.health.exception.banking.BankAccountAlreadyExists;
import com.umulam.fleen.health.exception.banking.InvalidAccountTypeCombinationException;
import com.umulam.fleen.health.exception.banking.InvalidBankCodeException;
import com.umulam.fleen.health.model.dto.banking.AddBankAccountDto;
import com.umulam.fleen.health.model.dto.banking.CreateWithdrawalDto;
import com.umulam.fleen.health.model.event.base.InternalPaymentValidation;
import com.umulam.fleen.health.model.event.base.WithdrawalTransferValidation;
import com.umulam.fleen.health.model.event.flutterwave.FwChargeEvent;
import com.umulam.fleen.health.model.event.flutterwave.FwTransferEvent;
import com.umulam.fleen.health.model.event.paystack.PsChargeEvent;
import com.umulam.fleen.health.model.event.paystack.PsTransferEvent;
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

@Slf4j
@Component
@Primary
public abstract class BankingServiceImpl implements BankingService {

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

  public InternalPaymentValidation getInternalPaymentValidationByChargeEvent(String body, PaymentGatewayType paymentGatewayType) {
    try {
      if (paymentGatewayType == PaymentGatewayType.FLUTTERWAVE) {
        FwChargeEvent event = mapper.readValue(body, FwChargeEvent.class);
        return InternalPaymentValidation.builder()
          .status(event.getData().getStatus().toUpperCase())
          .transactionReference(event.getData().getTransactionReference())
          .externalSystemTransactionReference(event.getData().getExternalSystemReference())
          .currency(event.getData().getCurrency().toUpperCase())
          .build();
      } else if (paymentGatewayType == PaymentGatewayType.PAYSTACK) {
        PsChargeEvent event = mapper.readValue(body, PsChargeEvent.class);
        return InternalPaymentValidation.builder()
          .status(event.getData().getStatus().toUpperCase())
          .transactionReference(event.getData().getMetadata().getTransactionReference())
          .externalSystemTransactionReference(event.getData().getReference())
          .currency(event.getData().getCurrency().toUpperCase())
          .build();
      }
    } catch (JsonProcessingException ex) {
      log.error(ex.getMessage(), ex);
    }
    return null;
  }

  public WithdrawalTransferValidation getWithdrawalTransferValidationByTransferEvent(String body, PaymentGatewayType paymentGatewayType) {
    try {
      if (paymentGatewayType == PaymentGatewayType.FLUTTERWAVE) {
        FwTransferEvent event = mapper.readValue(body, FwTransferEvent.class);
        return WithdrawalTransferValidation.builder()
          .reference(event.getData().getTransferReference())
          .amount(event.getData().getAmount())
          .bankCode(event.getData().getBankCode())
          .accountNumber(event.getData().getAccountNumber())
          .bankName(event.getData().getBankName())
          .fullName(event.getData().getFullName())
          .status(event.getData().getStatus())
          .fee(event.getData().getFee())
          .currency(event.getData().getCurrency())
          .build();
      } else if (paymentGatewayType == PaymentGatewayType.PAYSTACK) {
        PsTransferEvent event = mapper.readValue(body, PsTransferEvent.class);
        return WithdrawalTransferValidation.builder()
          .reference(event.getData().getReference())
          .amount(event.getData().getAmount())
          .bankCode(event.getData().getRecipient().getDetails().getBankCode())
          .accountNumber(event.getData().getRecipient().getDetails().getAccountNumber())
          .bankName(event.getData().getRecipient().getDetails().getBankName())
          .fullName(event.getData().getRecipient().getName())
          .status(event.getData().getStatus())
          .fee(0.00)
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
  public void createWithdrawal(CreateWithdrawalDto dto, FleenUser user) {

  }

  public static boolean isAccountTypeCombinationValid(String recipientType, String currencyType) {
    PaymentRecipientType recipient = PaymentRecipientType.valueOf(recipientType.toUpperCase());
    CurrencyType currency = CurrencyType.valueOf(currencyType.toUpperCase());

    switch (recipient) {
      case NUBAN:
        return currency == CurrencyType.NGN || currency == CurrencyType.KES || currency == CurrencyType.UGX;
      case MOBILE_MONEY:
        return currency == CurrencyType.GHS;
      case BASA:
        return currency == CurrencyType.ZAR;
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
}
