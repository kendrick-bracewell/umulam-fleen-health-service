package com.umulam.fleen.health.exception.authentication;

import com.umulam.fleen.health.exception.base.FleenHealthException;

public class DisabledAccountException extends FleenHealthException {

  private static final String message = "This account has been disabled";

  public DisabledAccountException() {
    super(message);
  }
}
 
