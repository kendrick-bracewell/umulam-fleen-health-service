package com.umulam.fleen.health.validator.impl;

import com.umulam.fleen.health.validator.DateValid;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.umulam.fleen.health.util.DateFormatUtil.DATE;
import static com.umulam.fleen.health.util.DateTimeUtil.isDateOrTimeValid;

@Slf4j
public class DateValidValidator implements ConstraintValidator<DateValid, String> {

  @Override
  public void initialize(DateValid constraintAnnotation) { }

  @Override
  public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
    return isDateOrTimeValid(date, DATE);
  }
}
