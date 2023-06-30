package com.umulam.fleen.health.exception.authentication;

import com.umulam.fleen.health.exception.base.FleenHealthException;

public class InvalidVerificationCodeException extends FleenHealthException {
  private static final String message = "Invalid verification code : %s";

  public InvalidVerificationCodeException(String code) {
    super(String.format(message, code));
  }
}
