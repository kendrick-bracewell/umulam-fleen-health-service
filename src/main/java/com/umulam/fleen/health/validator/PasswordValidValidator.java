package com.umulam.fleen.health.validator;

import lombok.extern.slf4j.Slf4j;
import org.passay.*;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

@Slf4j
@Component
public class PasswordValidValidator implements ConstraintValidator<PasswordValid, String> {

  @Override
  public void initialize(PasswordValid constraintAnnotation) { }

  @Override
  public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
    return validatePassword(password);
  }

  public boolean validatePassword(String password) {
    PasswordValidator validator = new PasswordValidator(null, Arrays.asList(
      new LengthRule(8, 24),
      new CharacterRule(EnglishCharacterData.UpperCase, 1),
      new CharacterRule(EnglishCharacterData.LowerCase, 1),
      new CharacterRule(EnglishCharacterData.Digit, 1),
      new CharacterRule(EnglishCharacterData.Special, 1),
      new WhitespaceRule()));

    RuleResult result = validator.validate(new PasswordData(password));
    return result.isValid();
  }
}
