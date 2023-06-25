package com.umulam.fleen.health.exception;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class RoleDuplicateException extends FleenHealthException {
  private static final String message = "Role is a duplicate of another entry. ID: %s";

  public RoleDuplicateException(String id) {
    super(String.format(message, Objects.toString(id, "Unknown")));
  }
}
