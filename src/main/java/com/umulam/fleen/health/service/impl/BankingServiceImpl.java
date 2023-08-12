package com.umulam.fleen.health.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umulam.fleen.health.constant.authentication.PaymentGatewayType;
import com.umulam.fleen.health.model.event.InternalPaymentValidation;
import com.umulam.fleen.health.model.event.flutterwave.FwChargeEvent;
import com.umulam.fleen.health.model.event.paystack.PsChargeEvent;
import com.umulam.fleen.health.service.BankingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BankingServiceImpl implements BankingService {

  private final ObjectMapper mapper;

  public BankingServiceImpl(ObjectMapper mapper) {
    this.mapper = mapper;
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
}
