package com.umulam.fleen.health.validator.impl;

import com.umulam.fleen.health.service.CountryService;
import com.umulam.fleen.health.validator.CountryCodeExist;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import static java.util.Objects.*;

@Slf4j
@Component
public class CountryCodeExistValidator implements ConstraintValidator<CountryCodeExist, String> {
  private final CountryService service;
  public CountryCodeExistValidator(CountryService service) {
    this.service = service;
  }

  @Override
  public void initialize(CountryCodeExist constraintAnnotation) {}

  @Override
  public boolean isValid(String code, ConstraintValidatorContext context) {
    if (!isNull(code)) {
      return !service.isCountryExistsByCode(code);
    }
    return false;
  }
  
}
