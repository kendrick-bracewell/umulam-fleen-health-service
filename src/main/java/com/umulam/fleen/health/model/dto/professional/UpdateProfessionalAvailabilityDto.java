package com.umulam.fleen.health.model.dto.professional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.professional.AvailabilityDayOfTheWeek;
import com.umulam.fleen.health.validator.EnumValid;
import com.umulam.fleen.health.validator.ValidAvailabilityEndTime;
import com.umulam.fleen.health.validator.ValidAvailabilityStartTime;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
  List<AvailabilityPeriod> periods;

  @Getter
  @Setter
  public static class AvailabilityPeriod {

    @EnumValid(enumClass = AvailabilityDayOfTheWeek.class)
    @JsonProperty("day_of_the_week")
    private String dayOfTheWeek;

    @NotNull
    @DateTimeFormat(iso = TIME)
    @ValidAvailabilityStartTime
    @JsonProperty("start_time")
    private LocalTime startTime;

    @NotNull
    @DateTimeFormat(iso = TIME)
    @ValidAvailabilityEndTime
    @JsonProperty("end_time")
    private LocalTime endTime;
  }
}
