package com.umulam.fleen.health.exception.healthsession;

import com.umulam.fleen.health.exception.base.FleenHealthException;
import com.umulam.fleen.health.model.response.FleenHealthResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class PatientProfessionalAlreadyBookSessionException extends FleenHealthException {

  private static final String message = "Patient already has a scheduled session with the Professional %s on date %s at time %s";

  public PatientProfessionalAlreadyBookSessionException(String professionalName, LocalDate date, LocalTime time) {
    super(String.format(message, Objects.toString(professionalName, "Unknown").trim(), date.toString(), time.toString()));
  }
}
