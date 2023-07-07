package com.umulam.fleen.health.exception.authentication;

import com.umulam.fleen.health.exception.base.FleenHealthException;
import com.umulam.fleen.health.model.response.FleenHealthResponse;

public class ResetPasswordCodeExpiredException extends FleenHealthException {

  private static final String message = "Reset password code is invalid or has expired.";

  public ResetPasswordCodeExpiredException() {
    super(message);
  }

}
