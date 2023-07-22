package com.umulam.fleen.health.exception.healthsession;

import com.umulam.fleen.health.exception.base.FleenHealthException;

public class HealthSessionInvalidTransactionException extends FleenHealthException {

  private static final String message = "Session invalid transaction";

  public HealthSessionInvalidTransactionException() {
    super(message);
  }
}
