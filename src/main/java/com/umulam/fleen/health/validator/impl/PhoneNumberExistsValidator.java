package com.umulam.fleen.health.validator.impl;

import com.umulam.fleen.health.service.MemberService;
import com.umulam.fleen.health.validator.PhoneNumberExists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
@Component
public class PhoneNumberExistsValidator implements ConstraintValidator<PhoneNumberExists, String> {

  private final MemberService service;

  public PhoneNumberExistsValidator(MemberService service) {
    this.service = service;
  }

  @Override
  public void initialize(PhoneNumberExists constraintAnnotation) {}

  @Override
  public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
    return !(service.isPhoneNumberExists(phoneNumber));
  }
}
