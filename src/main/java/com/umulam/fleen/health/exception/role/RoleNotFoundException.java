package com.umulam.fleen.health.exception.role;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class RoleNotFoundException extends FleenHealthException {

  private static final String message = "Role does not exists or cannot be found. ID: %s";

  public RoleNotFoundException(Object id) {
    super(String.format(message, Objects.toString(id, "Unknown")));
  }
}
