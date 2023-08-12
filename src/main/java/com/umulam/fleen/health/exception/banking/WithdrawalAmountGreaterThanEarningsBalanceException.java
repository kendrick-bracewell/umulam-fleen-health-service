package com.umulam.fleen.health.exception.banking;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class WithdrawalAmountGreaterThanEarningsBalanceException extends FleenHealthException {

  private static final String message = "Withdraw amount %s cannot be processed because final amount with fee %s is less than transferable threshold";

  public WithdrawalAmountGreaterThanEarningsBalanceException(Object withdrawAmount, Object fee) {
    super(String.format(message, Objects.toString(withdrawAmount, "Unknown"), Objects.toString(fee, "Unknown")));
  }
}
