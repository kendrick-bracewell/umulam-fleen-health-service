package com.umulam.fleen.health.validator.impl;

import com.umulam.fleen.health.repository.jpa.BankAccountJpaRepository;
import com.umulam.fleen.health.validator.BankAccountExist;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
@Component
public class BankAccountExistValidator implements ConstraintValidator<BankAccountExist, String> {

  private final BankAccountJpaRepository bankAccountJpaRepository;

  public BankAccountExistValidator(BankAccountJpaRepository bankAccountJpaRepository) {
    this.bankAccountJpaRepository = bankAccountJpaRepository;
  }

  @Override
  public void initialize(BankAccountExist constraintAnnotation) {}

  @Override
  public boolean isValid(String id, ConstraintValidatorContext context) {
    try {
      return bankAccountJpaRepository.existsById(Integer.parseInt(id));
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return false;
    }
  }
}
