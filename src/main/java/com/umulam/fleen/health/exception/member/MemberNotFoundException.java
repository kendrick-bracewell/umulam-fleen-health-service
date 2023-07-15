package com.umulam.fleen.health.exception.member;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class MemberNotFoundException extends FleenHealthException {

  private static final String message = "Member does not exists or cannot be found. ID: %s";

  public MemberNotFoundException(Object id) {
    super(String.format(message, Objects.toString(id, "Unknown")));
  }
}
