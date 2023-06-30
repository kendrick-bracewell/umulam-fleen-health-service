package com.umulam.fleen.health.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

public class EnumValidValidator implements ConstraintValidator<EnumValid, CharSequence> {
  private List<String> acceptedValues;

  @Override
  public void initialize(EnumValid annotation) {
    acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
            .map(Enum::name)
            .collect(Collectors.toList());
  }

  @Override
  public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
    if (!isNull(value)) {
      return acceptedValues.contains(value.toString());
    }
    return false;
  }

}
