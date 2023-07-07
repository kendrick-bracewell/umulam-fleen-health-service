package com.umulam.fleen.health.exception.authentication;

import com.umulam.fleen.health.exception.base.FleenHealthException;

public class VerificationFailedException extends FleenHealthException {

  private static final String message = "Verification failed";

  public VerificationFailedException() {
    super(message);
  }
}
