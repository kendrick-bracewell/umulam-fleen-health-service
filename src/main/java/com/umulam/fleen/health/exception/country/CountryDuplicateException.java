package com.umulam.fleen.health.exception.country;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class CountryDuplicateException extends FleenHealthException {

  private static final String message = "Country is a duplicate of another entry. ID: %s";

  public CountryDuplicateException(String id) {
    super(String.format(message, Objects.toString(id, "Unknown")));
  }
}
