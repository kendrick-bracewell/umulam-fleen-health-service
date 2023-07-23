package com.umulam.fleen.health.exception.transaction;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class SessionTransactionNotFound extends FleenHealthException {
  private static final String message = "Session Transaction does not exists or cannot be found. ID: %s";

  public SessionTransactionNotFound(Object id) {
    super(String.format(message, Objects.toString(id, "Unknown")));
  }

}
