package com.umulam.fleen.health.validator.impl;

import com.umulam.fleen.health.validator.NoDuplicateAvailabilityPeriod;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.umulam.fleen.health.model.dto.professional.UpdateProfessionalAvailabilityDto.AvailabilityPeriod;

@Slf4j
public class NoDuplicationAvailabilityPeriodValidator implements ConstraintValidator<NoDuplicateAvailabilityPeriod, List<AvailabilityPeriod>> {

  @Override
  public void initialize(NoDuplicateAvailabilityPeriod constraintAnnotation) { }

  @Override
  public boolean isValid(List<AvailabilityPeriod> availabilityPeriods, ConstraintValidatorContext constraintValidatorContext) {
    Set<AvailabilityPeriod> uniqueAvailabilitySet = new HashSet<>(availabilityPeriods);
    return availabilityPeriods.size() == uniqueAvailabilitySet.size();
  }
}
