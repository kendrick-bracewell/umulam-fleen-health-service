package com.umulam.fleen.health.exception.healthsession;

import com.umulam.fleen.health.exception.base.FleenHealthException;

public class SessionInvalidTransactionException extends FleenHealthException {

  private static final String message = "Session invalid transaction";

  public SessionInvalidTransactionException() {
    super(message);
  }
}
