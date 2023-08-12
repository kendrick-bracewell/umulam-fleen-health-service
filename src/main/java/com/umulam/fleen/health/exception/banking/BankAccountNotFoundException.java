package com.umulam.fleen.health.exception.banking;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class BankAccountNotFoundException extends FleenHealthException {

  private static final String message = "The bank account with number or ID %s does not exist or cannot be found";

  public BankAccountNotFoundException(Object accountNumber) {
    super(String.format(message, Objects.toString(accountNumber, "Unknown")));
  }
}
