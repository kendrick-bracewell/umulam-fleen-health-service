package com.umulam.fleen.health.exception.professional;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class ProfessionalNotAvailableForSessionException extends FleenHealthException {

  private static final String message = "Professional %s is unavailable for health sessions or appointments";

  public ProfessionalNotAvailableForSessionException(String professionalName) {
    super(String.format(message, Objects.toString(professionalName, "Unknown")));
  }
}
 
