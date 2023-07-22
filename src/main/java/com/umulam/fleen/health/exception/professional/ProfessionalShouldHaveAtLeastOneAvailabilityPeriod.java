package com.umulam.fleen.health.exception.professional;

import com.umulam.fleen.health.exception.base.FleenHealthException;

public class ProfessionalShouldHaveAtLeastOneAvailabilityPeriod extends FleenHealthException {

  private static final String message = "Professional should have at least one availability period or schedule set for booking sessions before updating availability status";

  public ProfessionalShouldHaveAtLeastOneAvailabilityPeriod() {
    super(message);
  }
}
