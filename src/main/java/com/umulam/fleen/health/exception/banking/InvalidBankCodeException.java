package com.umulam.fleen.health.exception.banking;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class InvalidBankCodeException extends FleenHealthException {

  private static final String message = "The provided bank code is invalid. CODE: %s";

  public InvalidBankCodeException(Object code) {
    super(String.format(message, Objects.toString(code, "Unknown")));
  }
}
