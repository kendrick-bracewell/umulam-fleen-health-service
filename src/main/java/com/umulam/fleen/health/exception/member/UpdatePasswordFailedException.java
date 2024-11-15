package com.umulam.fleen.health.exception.member;

import com.umulam.fleen.health.exception.base.FleenHealthException;

public class UpdatePasswordFailedException extends FleenHealthException {

  private static final String message = "Update password failed. Check your password and try again.";

  public UpdatePasswordFailedException() {
    super(message);
  }
}
 
