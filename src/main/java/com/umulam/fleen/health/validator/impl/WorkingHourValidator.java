package com.umulam.fleen.health.validator.impl;

import com.umulam.fleen.health.validator.WorkingHour;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.umulam.fleen.health.util.DateTimeUtil.validateWorkingHour;

@Slf4j
public class WorkingHourValidator implements ConstraintValidator<WorkingHour, String> {

  @Override
  public void initialize(WorkingHour constraintAnnotation) { }

  @Override
  public boolean isValid(String time, ConstraintValidatorContext constraintValidatorContext) {
    return validateWorkingHour(time);
  }
}
