package com.umulam.fleen.health.validator.impl;

import com.umulam.fleen.health.constant.professional.AvailabilityDayOfTheWeek;
import com.umulam.fleen.health.validator.MaxAvailabilityTimeInADay;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.*;
import java.util.stream.Collectors;

import static com.umulam.fleen.health.model.dto.professional.UpdateProfessionalAvailabilityDto.AvailabilityPeriod;

@Slf4j
public class MaxAvailabilityTimeInADayValidator implements ConstraintValidator<MaxAvailabilityTimeInADay, List<AvailabilityPeriod>> {

  @Override
  public void initialize(MaxAvailabilityTimeInADay constraintAnnotation) { }

  @Override
  public boolean isValid(List<AvailabilityPeriod> availabilityPeriods, ConstraintValidatorContext constraintValidatorContext) {
    if (!Objects.isNull(availabilityPeriods) && !availabilityPeriods.isEmpty()) {
      Map<AvailabilityDayOfTheWeek, List<String>> availabilityTimes = new HashMap<>();
      availabilityPeriods = availabilityPeriods
        .stream()
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

      for (AvailabilityPeriod period : availabilityPeriods) {
        try {
          AvailabilityDayOfTheWeek dayOfTheWeek = AvailabilityDayOfTheWeek.valueOf(period.getDayOfTheWeek());
          addToMap(availabilityTimes, dayOfTheWeek, null);
        } catch (IllegalArgumentException | NullPointerException ex) {
          log.error(ex.getMessage(), ex);
          return false;
        }
      }

      for (var entry: availabilityTimes.entrySet()) {
        final int TOTAL_NUMBER_OF_WORKING_HOURS = 9;
        if (entry.getValue().size() > TOTAL_NUMBER_OF_WORKING_HOURS) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  private static void addToMap(Map<AvailabilityDayOfTheWeek, List<String>> map, AvailabilityDayOfTheWeek key, String value) {
    // Check if the key already exists in the map
    if (map.containsKey(key)) {
      // Key exists, add the value to the existing list
      map.get(key).add(value);
    } else {
      // Key does not exist, create a new list and add the value
      List<String> newList = new ArrayList<>();
      newList.add(value);
      map.put(key, newList);
    }
  }
}
