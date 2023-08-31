package com.umulam.fleen.health.exception.member;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class EmailAddressAlreadyExistsException extends FleenHealthException {

  private static final String message = "Email Address already exists. Email: %s";

  public EmailAddressAlreadyExistsException(Object id) {
    super(String.format(message, Objects.toString(id, "Unknown")));
  }
}
