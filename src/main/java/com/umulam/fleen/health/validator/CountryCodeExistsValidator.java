package com.umulam.fleen.health.validator;

import com.umulam.fleen.health.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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
    return !service.isCountryExistsByCode(code);
  }
  
}
