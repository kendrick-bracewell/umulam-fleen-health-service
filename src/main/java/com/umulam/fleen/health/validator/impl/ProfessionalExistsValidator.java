package com.umulam.fleen.health.validator.impl;

import com.umulam.fleen.health.service.MemberService;
import com.umulam.fleen.health.validator.ProfessionalExists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
@Component
public class ProfessionalExistsValidator implements ConstraintValidator<ProfessionalExists, String> {

  private MemberService service;

  public ProfessionalExistsValidator(MemberService service) {
    this.service = service;
  }

  @Override
  public void initialize(ProfessionalExists constraintAnnotation) {}

  @Override
  public boolean isValid(String id, ConstraintValidatorContext constraintValidatorContext) {
    try {
      boolean exists = service.isMemberExists(Integer.parseInt(id));

    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return false;
    }
  }
}
