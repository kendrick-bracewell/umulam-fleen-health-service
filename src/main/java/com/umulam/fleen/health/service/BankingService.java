package com.umulam.fleen.health.service;

import com.umulam.fleen.health.model.event.InternalPaymentValidation;

public interface BankingService {

  String getTransactionStatusByReference(String transactionReference);

  InternalPaymentValidation getInternalPaymentValidationByChargeEvent(String body);
}
