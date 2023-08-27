package com.umulam.fleen.health.exception.base;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.RESOURCE_NOT_FOUND;

public class ResourceNotFoundException extends FleenHealthException {

  public static final String message = RESOURCE_NOT_FOUND;

  public ResourceNotFoundException() {
    super(message);
  }
}
