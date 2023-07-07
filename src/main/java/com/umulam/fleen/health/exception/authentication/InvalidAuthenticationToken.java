package com.umulam.fleen.health.exception.authentication;

import com.umulam.fleen.health.exception.base.FleenHealthException;

public class InvalidAuthenticationToken extends FleenHealthException {

  private static final String message = "The authentication token is invalid or does not exists";

  public InvalidAuthenticationToken() {
    super(message);
  }
}
