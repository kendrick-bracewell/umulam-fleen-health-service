package com.umulam.fleen.health.exception.healthsession;

import com.umulam.fleen.health.exception.base.FleenHealthException;

public class AddReviewAfterSessionCompleteException extends FleenHealthException {

  private static final String message = "You can only add review after the session complete";

  public AddReviewAfterSessionCompleteException() {
    super(message);
  }
}
