package com.umulam.fleen.health.validator.impl;

import com.umulam.fleen.health.validator.EmailValid;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.util.Objects.nonNull;

@Slf4j
public class EmailValidValidator implements ConstraintValidator<EmailValid, String> {

  @Override
  public void initialize(EmailValid constraintAnnotation) { }

  @Override
  public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
    if (nonNull(email)) {
      String pattern = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}";
      return email.matches(pattern);
    }
    return false;
  }
}
