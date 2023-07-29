package com.umulam.fleen.health.exception.banking;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class InvalidAccountTypeCombinationException extends FleenHealthException {

  private static final String message = "The account type %s and the currency %s do not match the requirement";

  public InvalidAccountTypeCombinationException(Object accountType, Object currency) {
    super(String.format(message, Objects.toString(accountType, "Unknown"), Objects.toString(currency, "Unknown")));
  }
}
