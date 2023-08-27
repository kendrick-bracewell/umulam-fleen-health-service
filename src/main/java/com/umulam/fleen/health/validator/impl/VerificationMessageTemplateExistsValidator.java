package com.umulam.fleen.health.validator.impl;

import com.umulam.fleen.health.service.ProfileVerificationMessageService;
import com.umulam.fleen.health.validator.VerificationMessageTemplateExists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
@Component
public class VerificationMessageTemplateExistsValidator implements ConstraintValidator<VerificationMessageTemplateExists, String> {

  private final ProfileVerificationMessageService service;

  public VerificationMessageTemplateExistsValidator(
          ProfileVerificationMessageService service) {
    this.service = service;
  }

  @Override
  public void initialize(VerificationMessageTemplateExists constraintAnnotation) { }

  @Override
  public boolean isValid(String id, ConstraintValidatorContext context) {
    try {
      return service.existsById(Long.parseLong(id));
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return false;
    }
  }
}

