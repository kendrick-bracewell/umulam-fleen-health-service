package com.umulam.fleen.health.exception.member;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class UserNotFoundException extends FleenHealthException {

  private static final String message = "User does not exists or cannot be found. ID: %s";

  public UserNotFoundException(String username) {
    super(String.format(message, Objects.toString(username, "Unknown")));
  }
}
