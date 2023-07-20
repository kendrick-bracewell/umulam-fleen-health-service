package com.umulam.fleen.health.exception.healthsession;

import com.umulam.fleen.health.exception.base.FleenHealthException;

public class SessionPaymentHasNotBeenConfirmedException extends FleenHealthException {

  private static final String message = "The payment associated to this session has not been confirmed";

  public SessionPaymentHasNotBeenConfirmedException() {
    super(message);
  }
}
