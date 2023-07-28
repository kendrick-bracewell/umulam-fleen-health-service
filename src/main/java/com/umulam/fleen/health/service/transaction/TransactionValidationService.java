package com.umulam.fleen.health.service.transaction;

import org.springframework.transaction.annotation.Transactional;

public interface TransactionValidationService {

  @Transactional
  void validateAndCompleteTransaction(String body);
}
