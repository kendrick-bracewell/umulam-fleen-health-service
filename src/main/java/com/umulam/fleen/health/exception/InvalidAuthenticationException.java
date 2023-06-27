package com.umulam.fleen.health.exception;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class InvalidAuthenticationException extends FleenHealthException {

  private static final String message = "The authentication details is invalid. ID: %s";

  public InvalidAuthenticationException(String emailAddress) {
    super(String.format(message, Objects.toString(emailAddress, "Unknown")));
  }
}
