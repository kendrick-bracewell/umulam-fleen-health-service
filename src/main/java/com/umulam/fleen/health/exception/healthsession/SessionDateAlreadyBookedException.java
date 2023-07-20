package com.umulam.fleen.health.exception.healthsession;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class SessionDateAlreadyBookedException extends FleenHealthException {

  private static final String message = "A session has already been book with the professional : %s at this date %s:%s";

  public SessionDateAlreadyBookedException(String professionalName, String date, String time) {
    super(String.format(message, Objects.toString(professionalName, "Unknown"), date, time));
  }
}
