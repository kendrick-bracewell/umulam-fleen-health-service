package com.umulam.fleen.health.exception.member;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class EmailAddressNotFoundException extends FleenHealthException {
  private static final String message = "Email Address does not exists or cannot be found. ID: %s";

  public EmailAddressNotFoundException(Object id) {
    super(String.format(message, Objects.toString(id, "Unknown")));
  }
}
 
