package com.umulam.fleen.health.exception.memberstatus;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class MemberStatusNotFoundException extends FleenHealthException {

  private static final String message = "Member Status does not exists or cannot be found. ID: %s";

  public MemberStatusNotFoundException(Object id) {
    super(String.format(message, Objects.toString(id, "Unknown")));
  }
}
 
 
