package com.umulam.fleen.health.exception.authentication;

import com.umulam.fleen.health.exception.base.FleenHealthException;

public class MfaVerificationFailed extends FleenHealthException {

  private static final String message = "An error occurred while verifying the Multi-Factor Authentication (MFA) setup";

  public MfaVerificationFailed() {
    super(message);
  }
}
 
