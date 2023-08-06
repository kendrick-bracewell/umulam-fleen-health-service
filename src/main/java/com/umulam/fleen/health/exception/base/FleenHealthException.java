package com.umulam.fleen.health.exception.base;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.UNABLE_TO_COMPLETE_OPERATION;

public class FleenHealthException extends RuntimeException {

  public FleenHealthException(String message) {
    super(message);
  }

  public FleenHealthException() {
    super(UNABLE_TO_COMPLETE_OPERATION);
  }
}
