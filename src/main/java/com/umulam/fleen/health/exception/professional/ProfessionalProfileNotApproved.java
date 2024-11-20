package com.umulam.fleen.health.exception.professional;

import com.umulam.fleen.health.exception.base.FleenHealthException;

public class ProfessionalProfileNotApproved extends FleenHealthException {

  private static final String message = "Professional profile or verification status is not approved";

  public ProfessionalProfileNotApproved() {
    super(message);
  }
}
 
