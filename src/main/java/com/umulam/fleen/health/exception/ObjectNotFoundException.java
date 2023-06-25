package com.umulam.fleen.health.exception;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class ObjectNotFoundException extends FleenHealthException {

  private static final String message = "Object does not exists or cannot be found. KEY: %s";

  public ObjectNotFoundException(String key) {
    super(String.format(message, Objects.toString(key, "Unknown")));
  }
}
