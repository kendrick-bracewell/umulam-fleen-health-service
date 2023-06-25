package com.umulam.fleen.health.exception;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class CountryNotFoundException extends FleenHealthException {

  private static final String message = "Country does not exists or cannot be found. ID: %s";

  public CountryNotFoundException(Object id) {
    super(String.format(message, Objects.toString(id, "Unknown")));
  }
}
