package com.umulam.fleen.health.exception.memberstatus;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class MemberStatusCodeDuplicateException extends FleenHealthException {

  private static final String message = "Role is a duplicate of another entry. ID: %s";

  public MemberStatusCodeDuplicateException(String id) {
    super(String.format(message, Objects.toString(id, "Unknown")));
  }
}
