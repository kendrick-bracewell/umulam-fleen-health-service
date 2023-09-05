package com.umulam.fleen.health.exception.professional;

import com.umulam.fleen.health.exception.base.FleenHealthException;
import lombok.Getter;

@Getter
public class HasNoProfessionalProfileException extends FleenHealthException {
  private static final String message = "No Professional profile associated with member. Please create one.";
  public final String TYPE = "NO_PROFILE";

  public HasNoProfessionalProfileException() {
    super(message);
  }

}
