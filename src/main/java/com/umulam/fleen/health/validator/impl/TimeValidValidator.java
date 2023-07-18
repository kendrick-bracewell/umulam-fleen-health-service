package com.umulam.fleen.health.validator.impl;

import com.umulam.fleen.health.validator.TimeValid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.umulam.fleen.health.util.DateFormatUtil.TIME;
import static com.umulam.fleen.health.util.DateTimeUtil.isDateOrTimeValid;

@Slf4j
@Component
public class TimeValidValidator implements ConstraintValidator<TimeValid, String> {

  @Override
  public void initialize(TimeValid constraintAnnotation) { }

  @Override
  public boolean isValid(String time, ConstraintValidatorContext constraintValidatorContext) {
    return isDateOrTimeValid(time, TIME);
  }
}
