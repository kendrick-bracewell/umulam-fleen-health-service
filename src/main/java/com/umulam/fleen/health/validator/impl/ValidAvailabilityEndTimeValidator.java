package com.umulam.fleen.health.validator.impl;

import com.umulam.fleen.health.validator.ValidAvailabilityEndTime;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.umulam.fleen.health.util.DateFormatUtil.TIME;
import static java.util.Objects.nonNull;

@Slf4j
public class ValidAvailabilityEndTimeValidator implements ConstraintValidator<ValidAvailabilityEndTime, String> {

  LocalTime workingHoursStart = LocalTime.of(9, 0);
  LocalTime workingHoursEnd = LocalTime.of(18, 0);

  @Override
  public void initialize(ValidAvailabilityEndTime constraintAnnotation) {}

  @Override
  public boolean isValid(String time, ConstraintValidatorContext constraintValidatorContext) {
    if (nonNull(time)) {
      try {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(TIME);
        LocalTime startTime = LocalTime.parse(time, dtf);
        return startTime.isBefore(workingHoursStart) && !startTime.isAfter(workingHoursEnd);
      } catch (DateTimeParseException ex) {
        log.error(ex.getMessage(), ex);
      }
    }
    return false;
  }
}
