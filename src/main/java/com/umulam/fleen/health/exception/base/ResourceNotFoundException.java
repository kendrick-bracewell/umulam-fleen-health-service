package com.umulam.fleen.health.exception.base;

public class ResourceNotFoundException extends FleenHealthException {

  public static final String message = "The resource you are looking for does not exists or has been moved";

  public ResourceNotFoundException() {
    super(message);
  }
}
