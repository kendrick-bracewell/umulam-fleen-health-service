package com.umulam.fleen.health.validator;

import com.umulam.fleen.health.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
@Component
public class CountryExistsValidator implements ConstraintValidator<CountryExists, String> {

  private final CountryService countryService;
  public CountryExistsValidator(CountryService countryService) {
    this.countryService = countryService;
  }

  @Override
  public void initialize(CountryExists countryExists) {}

  @Override
  public boolean isValid(String id, ConstraintValidatorContext context) {
    try {
      return countryService.isCountryExists(Integer.parseInt(id));
    } catch (Exception ex) {
      log.error(ex.getMessage());
      return false;
    }
  }
}
