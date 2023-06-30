package com.umulam.fleen.health.exception.country;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class CountryCodeDuplicateException extends FleenHealthException {

  private static final String message = "Country code is a duplicate of another entry. code: %s";

  public CountryCodeDuplicateException(String id) {
    super(String.format(message, Objects.toString(id, "Unknown")));
  }
}
