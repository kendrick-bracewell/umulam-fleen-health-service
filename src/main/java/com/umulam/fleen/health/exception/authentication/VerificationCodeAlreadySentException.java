package com.umulam.fleen.health.exception.authentication;

import com.umulam.fleen.health.exception.base.FleenHealthException;

public class VerificationCodeAlreadySentException extends FleenHealthException {

  private static final String message = "Verification code has already been sent.";

  public VerificationCodeAlreadySentException() {
    super(message);
  }
}
