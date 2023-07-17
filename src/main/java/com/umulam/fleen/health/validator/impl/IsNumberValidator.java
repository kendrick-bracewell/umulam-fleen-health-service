package com.umulam.fleen.health.validator.impl;

import com.umulam.fleen.health.validator.IsNumber;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class IsNumberValidator implements ConstraintValidator<IsNumber, String> {

  @Override 
  public void initialize(IsNumber isNumber) {}

  @Override
  public boolean isValid(String number, ConstraintValidatorContext context) {
    try {
      return NumberUtils.isParsable(number);
    } catch (NumberFormatException ex) {
      log.error(ex.getMessage(), ex);
    }
    return false;
  }
}
