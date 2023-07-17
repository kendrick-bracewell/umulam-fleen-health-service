package com.umulam.fleen.health.model.dto.professional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.professional.AvailabilityDayOfTheWeek;
import com.umulam.fleen.health.validator.*;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

import static com.umulam.fleen.health.util.DateTimeUtil.toTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfessionalAvailabilityDto {

  @Valid
  @NotEmpty
  @Size(max = 63)
  @MaxAvailabilityTimeInADay
  @NoDuplicateAvailabilityPeriod
  @NoAvailabilityPeriodOverlap
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

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }

      if (!(o instanceof AvailabilityPeriod)) {
        return false;
      }

      AvailabilityPeriod that = (AvailabilityPeriod) o;
      return Objects.equals(
          AvailabilityDayOfTheWeek.valueOf(dayOfTheWeek),
          AvailabilityDayOfTheWeek.valueOf(that.dayOfTheWeek)) &&
        Objects.equals(toTime(startTime), toTime(that.startTime)) &&
        Objects.equals(toTime(endTime), toTime(that.endTime));
    }

    @Override
    public int hashCode() {
      return Objects.hash(dayOfTheWeek, startTime, endTime);
    }

    public boolean overlapsWith(AvailabilityPeriod otherEntry) {
      return Objects.equals(dayOfTheWeek, otherEntry.dayOfTheWeek) &&
        Objects.requireNonNull(toTime(startTime)).isBefore(toTime(otherEntry.endTime)) &&
        Objects.requireNonNull(toTime(endTime)).isAfter(toTime(otherEntry.startTime));
    }
  }
}
