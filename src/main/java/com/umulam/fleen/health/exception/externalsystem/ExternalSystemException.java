package com.umulam.fleen.health.exception.externalsystem;

import com.umulam.fleen.health.exception.base.FleenHealthException;

public class ExternalSystemException extends FleenHealthException {

  private static final String message = "Error occurred while processing your request: %s";

  public ExternalSystemException(String externalSystem) {
    super(String.format(message, externalSystem));
  }
}
 
