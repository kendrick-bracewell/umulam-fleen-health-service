package com.umulam.fleen.health.exception.professional;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class ProfessionalNotAvailableForSessionDayException extends FleenHealthException {

  private static final String message = "Professional %s is unavailable for health sessions or appointments on this day %s of the week";

  public ProfessionalNotAvailableForSessionDayException(String professionalName, String dayOfTheWeek) {
    super(String.format(message, Objects.toString(professionalName, "Unknown"), dayOfTheWeek));
  }
}
