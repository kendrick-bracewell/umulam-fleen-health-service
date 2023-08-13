package com.umulam.fleen.health.exception.banking;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class InsufficientEarningsBalanceException extends FleenHealthException {

  private static final String message = "Insufficient balance to withdraw %s because balance is %s";

  public InsufficientEarningsBalanceException(Object withdrawAmount, Object balance) {
    super(String.format(message, Objects.toString(withdrawAmount, "Unknown"), Objects.toString(balance, "Unknown")));
  }
}
