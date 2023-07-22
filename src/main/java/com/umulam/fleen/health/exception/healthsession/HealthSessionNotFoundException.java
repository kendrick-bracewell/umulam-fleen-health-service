package com.umulam.fleen.health.exception.healthsession;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class HealthSessionNotFoundException extends FleenHealthException {

  private static final String message = "Health session does not exist or cannot be found. ID: %s";

  public HealthSessionNotFoundException(Object id) {
    super(String.format(message, Objects.toString(id, "Unknown")));
  }
}
