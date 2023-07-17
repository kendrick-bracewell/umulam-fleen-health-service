package com.umulam.fleen.health.validator.impl;

import com.umulam.fleen.health.validator.DateOfBirth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import static com.umulam.fleen.health.util.DateFormatUtil.DATE;

@Slf4j
@Component
public class DateOfBirthValidator implements ConstraintValidator<DateOfBirth, String> {

  @Override
  public void initialize(DateOfBirth constraintAnnotation) {}

  @Override
  public boolean isValid(String date, ConstraintValidatorContext context) {
    if (Objects.nonNull(date)) {
      try {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE);
        LocalDate dateOfBirth = LocalDate.parse(date, dtf);
        return dateOfBirth.getYear() < LocalDate.now().getYear();
      } catch (DateTimeParseException ex) {
        log.error(ex.getMessage(), ex);
      }
    }
    return false;
  }

}
