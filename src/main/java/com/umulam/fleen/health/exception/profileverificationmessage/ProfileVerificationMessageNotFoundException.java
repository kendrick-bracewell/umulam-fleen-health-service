package com.umulam.fleen.health.exception.profileverificationmessage;

import com.umulam.fleen.health.exception.base.FleenHealthException;

import java.util.Objects;

public class ProfileVerificationMessageNotFoundException extends FleenHealthException {

  private static final String message = "Professional Verification Message does not exists or cannot be found. ID: %s";

  public ProfileVerificationMessageNotFoundException(Object id) {
    super(String.format(message, Objects.toString(id, "Unknown")));
  }

}
