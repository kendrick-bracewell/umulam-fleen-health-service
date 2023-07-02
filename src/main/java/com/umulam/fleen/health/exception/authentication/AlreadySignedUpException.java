package com.umulam.fleen.health.exception.authentication;

import com.umulam.fleen.health.exception.base.FleenHealthException;

public class AlreadySignedUpException extends FleenHealthException {

  private static final String message = "This profile is already signed up and has completed the registration process";

  public AlreadySignedUpException() {
    super(message);
  }
}
