package com.umulam.fleen.health.exception.banking;

import com.umulam.fleen.health.exception.base.FleenHealthException;

public class EarningsAccountNotFoundException extends FleenHealthException {

  private static final String message = "Earnings account or balance does not exists or cannot be found";

  public EarningsAccountNotFoundException() {
    super(message);
  }
}
