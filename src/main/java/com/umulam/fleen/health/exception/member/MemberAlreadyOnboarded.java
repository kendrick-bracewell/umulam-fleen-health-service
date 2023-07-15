package com.umulam.fleen.health.exception.member;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class MemberAlreadyOnboarded extends FleenHealthException {

  private static final String message = "Invalid process, member has already been onboarded. ID: %s";

  public MemberAlreadyOnboarded(String username) {
    super(String.format(message, Objects.toString(username, "Unknown")));
  }
}
