package com.umulam.fleen.health.service.transaction;

import com.umulam.fleen.health.constant.authentication.PaymentGatewayType;
import org.springframework.transaction.annotation.Transactional;

public interface TransactionValidationService {

  @Transactional
  void validateAndCompleteTransaction(String body);
}
