package com.umulam.fleen.health.exception.professional;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class ProfessionalNotFoundException extends FleenHealthException {

  private static final String message = "Professional does not exists or cannot be found. ID: %s";

  public ProfessionalNotFoundException(String username) {
    super(String.format(message, Objects.toString(username, "Unknown")));
  }
}
