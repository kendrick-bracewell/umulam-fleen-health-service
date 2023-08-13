package com.umulam.fleen.health.exception.banking;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class InvalidBankAccountOperationException extends FleenHealthException {

  private static final String message = "Invalid operation on bank account with number %s.";

  public InvalidBankAccountOperationException(Object id) {
    super(String.format(message, Objects.toString(id, "Unknown")));
  }
}
