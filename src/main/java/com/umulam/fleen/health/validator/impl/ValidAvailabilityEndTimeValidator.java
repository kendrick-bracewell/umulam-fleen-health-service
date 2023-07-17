package com.umulam.fleen.health.validator.impl;

import com.umulam.fleen.health.validator.ValidAvailabilityEndTime;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalTime;
import java.util.Objects;

@Slf4j
public class ValidAvailabilityEndTimeValidator implements ConstraintValidator<ValidAvailabilityEndTime, LocalTime> {

  LocalTime workingHoursStart = LocalTime.of(9, 0);
  LocalTime workingHoursEnd = LocalTime.of(18, 0);

  @Override
  public void initialize(ValidAvailabilityEndTime constraintAnnotation) {}

  @Override
  public boolean isValid(LocalTime startTime, ConstraintValidatorContext constraintValidatorContext) {
    return !Objects.isNull(startTime) && !startTime.isBefore(workingHoursStart) && !startTime.isAfter(workingHoursEnd);
  }
}
