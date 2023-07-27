package com.umulam.fleen.health.validator.impl;

import com.umulam.fleen.health.validator.EnumOrdinalValid;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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

  public List<Integer> getValues(Class<?> enumClass) {
    List<Integer> values = new ArrayList<>();
    try {
      Method valuesMethod = enumClass.getMethod("values");
      Object[] allEnums = (Object[]) valuesMethod.invoke(null);

      for (Object enumValue : allEnums) {
        Method ordinalMethod = enumClass.getMethod("ordinal");
        int ordinalValue = (int) ordinalMethod.invoke(enumValue);
        values.add(ordinalValue);
      }
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
      log.error(ex.getMessage(), ex);
    }
    return values;
  }
}
