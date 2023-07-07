package com.umulam.fleen.health.exception.authentication;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class InvalidAuthenticationException extends FleenHealthException {

  private static final String message = "The username or password credential is invalid. ID: %s";

  public InvalidAuthenticationException(String emailAddress) {
    super(String.format(message, Objects.toString(emailAddress, "Unknown")));
  }
}
