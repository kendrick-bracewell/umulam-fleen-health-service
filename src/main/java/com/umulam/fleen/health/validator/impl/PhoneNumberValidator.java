package com.umulam.fleen.health.validator.impl;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.umulam.fleen.health.validator.MobilePhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import static java.util.Objects.isNull;

@Slf4j
@Component
public class PhoneNumberValidator implements ConstraintValidator<MobilePhoneNumber, String> {

  @Override
  public void initialize(MobilePhoneNumber isNumber) {}

  @Override
  public boolean isValid(String number, ConstraintValidatorContext context) {
    if (!isNull(number)) {
      PhoneNumberUtil util = PhoneNumberUtil.getInstance();
      PhoneNumber phoneNumber;
      try {
        phoneNumber = util.parse(number, null);
      } catch (NumberParseException ex) {
        log.error(ex.getMessage(), ex);
        return false;
      }
      return util.isValidNumber(phoneNumber);
    }
    return true;
  }

}
