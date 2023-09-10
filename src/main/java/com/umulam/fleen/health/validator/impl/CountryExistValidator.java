package com.umulam.fleen.health.validator.impl;

import com.umulam.fleen.health.service.CountryService;
import com.umulam.fleen.health.validator.CountryExist;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
@Component
public class CountryExistValidator implements ConstraintValidator<CountryExist, String> {

  private final CountryService service;
  public CountryExistValidator(CountryService service) {
    this.service = service;
  }

  @Override
  public void initialize(CountryExist constraintAnnotation) {}

  @Override
  public boolean isValid(String id, ConstraintValidatorContext context) {
    System.out.println("The value is sososososos " + id);
    try {
      return service.isCountryExists(Long.parseLong(id));
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return false;
    }
  }
}
