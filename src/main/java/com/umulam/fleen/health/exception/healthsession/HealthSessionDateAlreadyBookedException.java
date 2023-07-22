package com.umulam.fleen.health.exception.healthsession;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class HealthSessionDateAlreadyBookedException extends FleenHealthException {

  private static final String message = "A session has already been book with the professional : %s at this date %s:%s";

  public HealthSessionDateAlreadyBookedException(String professionalName, LocalDate date, LocalTime time) {
    super(String.format(message, Objects.toString(professionalName, "Unknown"), date.toString(), time.toString()));
  }
}
