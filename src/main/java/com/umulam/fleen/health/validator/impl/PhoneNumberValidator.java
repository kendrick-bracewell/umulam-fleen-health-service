package com.umulam.fleen.health.validator.impl;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.umulam.fleen.health.validator.MobilePhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import static java.util.Objects.nonNull;

@Slf4j
@Component
public class PhoneNumberValidator implements ConstraintValidator<MobilePhoneNumber, String> {

  @Override
  public void initialize(MobilePhoneNumber constraintAnnotation) {}

  @Override
  public boolean isValid(String number, ConstraintValidatorContext context) {
    if (nonNull(number)) {
      PhoneNumberUtil util = PhoneNumberUtil.getInstance();
      PhoneNumber phoneNumber;
      try {
        phoneNumber = util.parse(number, null);
        return util.isValidNumber(phoneNumber);
      } catch (NumberParseException ex) {
        log.error(ex.getMessage(), ex);
      }
    }
    return false;
  }

}
 
