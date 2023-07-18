package com.umulam.fleen.health.validator.impl;

import com.umulam.fleen.health.service.MemberService;
import com.umulam.fleen.health.validator.MemberExists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
@Component
public class MemberExistsValidator implements ConstraintValidator<MemberExists, String> {

  private MemberService service;

  public MemberExistsValidator(MemberService service) {
    this.service = service;
  }

  @Override
  public void initialize(MemberExists constraintAnnotation) {}

  @Override
  public boolean isValid(String id, ConstraintValidatorContext constraintValidatorContext) {
    try {
      return service.isMemberExists(Integer.parseInt(id));
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return false;
    }
  }
}
