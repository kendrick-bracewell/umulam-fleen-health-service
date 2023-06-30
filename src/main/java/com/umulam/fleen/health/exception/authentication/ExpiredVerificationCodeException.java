package com.umulam.fleen.health.exception.authentication;

import com.umulam.fleen.health.exception.base.FleenHealthException;

public class ExpiredVerificationCodeException extends FleenHealthException {

  private static final String message = "Verification code expired";

  public ExpiredVerificationCodeException() {
    super(message);
  }
}
