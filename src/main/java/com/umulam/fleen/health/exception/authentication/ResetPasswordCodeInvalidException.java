package com.umulam.fleen.health.exception.authentication;

import com.umulam.fleen.health.exception.base.FleenHealthException;

public class ResetPasswordCodeInvalidException extends FleenHealthException {

  private static final String message = "Reset password code is invalid or has expired.";

  public ResetPasswordCodeInvalidException() {
    super(message);
  }
}
