package com.umulam.fleen.health.exception.authentication;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class VerificationFailedException extends FleenHealthException {

  private static final String message = "Verification failed";

  public VerificationFailedException() {
    super(message);
  }

  public VerificationFailedException(String message) {
    super(Objects.nonNull(message) ? message : VerificationFailedException.message);
  }
}
 
