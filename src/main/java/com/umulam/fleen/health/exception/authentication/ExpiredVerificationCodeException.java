package com.umulam.fleen.health.exception.authentication;

import com.umulam.fleen.health.exception.base.FleenHealthException;

public class ExpiredVerificationCodeException extends FleenHealthException {

  private static final String message = "Verification code has expired. CODE : %s";

  public ExpiredVerificationCodeException(String code) {
    super(String.format(message, code));
  }
}
