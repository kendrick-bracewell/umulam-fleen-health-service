package com.umulam.fleen.health.exception.member;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class PhoneNumberAlreadyExistsException extends FleenHealthException {

  private static final String message = "Phone Address already exists. Phone: %s";

  public PhoneNumberAlreadyExistsException(Object id) {
    super(String.format(message, Objects.toString(id, "Unknown")));
  }
}
