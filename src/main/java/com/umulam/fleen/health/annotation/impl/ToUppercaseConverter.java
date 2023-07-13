package com.umulam.fleen.health.annotation.impl;

import com.fasterxml.jackson.databind.util.StdConverter;

public class ToUppercaseConverter extends StdConverter<String, String> {
  @Override
  public String convert(String value) {
    return value == null ? null : value.toUpperCase();
  }
}
