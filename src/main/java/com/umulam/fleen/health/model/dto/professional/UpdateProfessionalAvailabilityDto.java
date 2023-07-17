package com.umulam.fleen.health.model.dto.professional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.professional.AvailabilityDayOfTheWeek;
import com.umulam.fleen.health.validator.*;
import com.umulam.fleen.health.validator.impl.TimeValidValidator;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.ISO.TIME;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfessionalAvailabilityDto {

  @Valid
  @NotEmpty
  @Size(max = 81)
  @MaxAvailabilityTimeInADay
  List<AvailabilityPeriod> periods;

  @Getter
  @Setter
  public static class AvailabilityPeriod {

    @EnumValid(enumClass = AvailabilityDayOfTheWeek.class)
    @JsonProperty("day_of_the_week")
    private String dayOfTheWeek;

    @NotNull
    @TimeValid
    @ValidAvailabilityStartTime
    @JsonProperty("start_time")
    private String startTime;

    @NotNull
    @TimeValid
    @ValidAvailabilityEndTime
    @JsonProperty("end_time")
    private String endTime;
  }
}
