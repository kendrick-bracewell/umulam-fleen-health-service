package com.umulam.fleen.health.exception.banking;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class BankAccountAlreadyExists extends FleenHealthException {

  private static final String message = "Bank account with number %s already exists";

  public BankAccountAlreadyExists(Object id) {
    super(String.format(message, Objects.toString(id, "Unknown")));
  }
}
