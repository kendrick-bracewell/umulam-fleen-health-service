package com.umulam.fleen.health.validator.impl;

import com.umulam.fleen.health.service.CountryService;
import com.umulam.fleen.health.validator.CountryCodeExists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import static java.util.Objects.*;

@Slf4j
@Component
public class CountryCodeExistsValidator implements ConstraintValidator<CountryCodeExists, String> {
  private final CountryService service;
  public CountryCodeExistsValidator(CountryService service) {
    this.service = service;
  }

  @Override
  public void initialize(CountryCodeExists countryExists) {}

  @Override
  public boolean isValid(String code, ConstraintValidatorContext context) {
    if (!isNull(code)) {
      return !service.isCountryExistsByCode(code);
    }
    return false;
  }
  
}
