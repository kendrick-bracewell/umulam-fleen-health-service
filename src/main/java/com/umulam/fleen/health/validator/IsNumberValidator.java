package com.umulam.fleen.health.validator;

import org.apache.commons.lang3.math.NumberUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsNumberValidator implements ConstraintValidator<IsNumber, String> {

  @Override
  public void initialize(IsNumber isNumber) {}

  @Override
  public boolean isValid(String number, ConstraintValidatorContext context) {
    return NumberUtils.isParsable(number);
  }
}
