package com.umulam.fleen.health.exception.business;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class BusinessNotFoundException extends FleenHealthException {

  private static final String message = "Business does not exists or cannot be found. ID: %s";

  public BusinessNotFoundException(Object id) {
    super(String.format(message, Objects.toString(id, "Unknown")));
  }
}
