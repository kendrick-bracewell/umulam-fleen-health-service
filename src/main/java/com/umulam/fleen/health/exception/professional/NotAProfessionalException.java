package com.umulam.fleen.health.exception.professional;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class NotAProfessionalException extends FleenHealthException {

  private static final String message = "Not a professional. ID : %s";

  public NotAProfessionalException(Object id) {
    super(String.format(message, Objects.toString(id, "Unknown")));
  }
}
