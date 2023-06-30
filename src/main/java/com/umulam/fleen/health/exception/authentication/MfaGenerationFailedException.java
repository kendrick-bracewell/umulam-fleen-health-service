package com.umulam.fleen.health.exception.authentication;

import com.umulam.fleen.health.exception.base.FleenHealthException;

public class MfaGenerationFailedException extends FleenHealthException {

  private static final String message = "Error occurred during MFA setup";

  public MfaGenerationFailedException() {
    super(message);
  }
}
