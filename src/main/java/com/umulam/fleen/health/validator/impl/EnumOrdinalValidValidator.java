package com.umulam.fleen.health.validator.impl;

import com.umulam.fleen.health.validator.EnumOrdinalValid;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

import static com.umulam.fleen.health.util.EnumUtil.getValues;
import static java.util.Objects.isNull;

@Slf4j
public class EnumOrdinalValidValidator implements ConstraintValidator<EnumOrdinalValid, String> {

  private List<Integer> acceptedValues;

  @Override
  public void initialize(EnumOrdinalValid constraintAnnotation) {
    acceptedValues = getValues(constraintAnnotation.enumClass());
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
    if (!isNull(value)) {
      try {
        Integer number = Integer.parseInt(value);
        return acceptedValues.contains(number);
      } catch (NumberFormatException ex) {
        log.error(ex.getMessage(), ex);
      }
    }
    return false;
  }

}
