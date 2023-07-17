package com.umulam.fleen.health.validator.impl;

import com.umulam.fleen.health.validator.TimeValid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import static com.umulam.fleen.health.util.DateFormatUtil.TIME;

@Slf4j
@Component
public class TimeValidValidator implements ConstraintValidator<TimeValid, String> {

  @Override
  public void initialize(TimeValid constraintAnnotation) { }

  @Override
  public boolean isValid(String time, ConstraintValidatorContext constraintValidatorContext) {
    if (Objects.nonNull(time)) {
      try {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(TIME);
        dtf.parse(time);
        return true;
      } catch (DateTimeParseException ex) {
        log.error(ex.getMessage(), ex);
      }
    }
    return false;  }
}
