package com.umulam.fleen.health.exception.healthsession;

import com.umulam.fleen.health.exception.base.FleenHealthException;

public class HealthSessionCanceledAlreadyException extends FleenHealthException {

  private static final String message = "Session has already been canceled";

  public HealthSessionCanceledAlreadyException() {
    super(message);
  }
}
