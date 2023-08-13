package com.umulam.fleen.health.service;

import com.umulam.fleen.health.model.domain.transaction.WithdrawalTransaction;

public interface EarningsService {

  void reverseTransactionAndUpdateEarnings(WithdrawalTransaction transaction);
}
