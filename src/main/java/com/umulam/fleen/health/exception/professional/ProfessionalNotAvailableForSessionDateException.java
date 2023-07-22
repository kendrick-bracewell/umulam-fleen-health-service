package com.umulam.fleen.health.exception.professional;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.umulam.fleen.health.util.DateTimeUtil.asDateTimeWithNoSeconds;

public class ProfessionalNotAvailableForSessionDateException extends FleenHealthException {

  private static final String message = "Professional %s not available for session at proposed date %s";

  public ProfessionalNotAvailableForSessionDateException(String professionalName, LocalDateTime dateTime) {
    super(String.format(message, Objects.toString(professionalName, "Unknown"), asDateTimeWithNoSeconds(dateTime)));
  }
}
