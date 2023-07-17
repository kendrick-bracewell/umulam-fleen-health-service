package com.umulam.fleen.health.model.dto.professional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.professional.AvailabilityDayOfTheWeek;
import com.umulam.fleen.health.validator.EnumValid;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.ISO.TIME;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfessionalAvailabilityDto {

  @NotEmpty
  List<AvailabilityPeriod> periods;

  @Getter
  @Setter
  public static class AvailabilityPeriod {

    @NotEmpty
    @EnumValid(enumClass = AvailabilityDayOfTheWeek.class)
    @JsonProperty("day_of_the_week")
    private String dayOfTheWeek;

    @NotEmpty
    @DateTimeFormat(iso = TIME)
    @JsonProperty("start_time")
    private LocalTime startTime;

    @NotEmpty
    @DateTimeFormat(iso = TIME)
    @JsonProperty("end_time")
    private LocalTime endTime;
  }
}
