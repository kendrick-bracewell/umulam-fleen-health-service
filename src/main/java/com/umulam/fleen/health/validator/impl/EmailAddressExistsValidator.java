package com.umulam.fleen.health.validator.impl;

import com.umulam.fleen.health.service.MemberService;
import com.umulam.fleen.health.validator.EmailAddressExists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.util.Objects.isNull;

@Slf4j
@Component
public class EmailAddressExistsValidator implements ConstraintValidator<EmailAddressExists, String> {

  private final MemberService service;

  public EmailAddressExistsValidator(MemberService service) {
    this.service = service;
  }

  @Override
  public void initialize(EmailAddressExists emailAddressExists) {}

  @Override
  public boolean isValid(String emailAddress, ConstraintValidatorContext context) {
    if (!isNull(emailAddress)) {
      return !(service.isEmailAddressExists(emailAddress.trim().toLowerCase()));
    }
    return true;
  }
}

