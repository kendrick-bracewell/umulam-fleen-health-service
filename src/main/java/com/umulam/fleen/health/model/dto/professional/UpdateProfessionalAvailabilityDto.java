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
  @NotEmpty(message = "{period.notEmpty}")
  @Size(min = 1, max = 63, message = "{period.size}")
  @MaxAvailabilityTimeInADay(message = "{period.maxAvailabilityInADay}")
  @NoDuplicateAvailabilityPeriod(message = "{period.noDuplicate}")
  @NoAvailabilityPeriodOverlap(message = "{period.noOverlap}")
  List<AvailabilityPeriod> periods;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class AvailabilityPeriod {

    @NotNull(message = "{period.dayOfTheWeek.notNull}")
    @EnumValid(enumClass = AvailabilityDayOfTheWeek.class, message = "{period.dayOfTheWeek.type}")
    @JsonProperty("day_of_the_week")
    private String dayOfTheWeek;

    @NotNull(message = "{period.startTime.notNull}")
    @TimeValid(message = "{period.startTime.valid}")
    @ValidAvailabilityStartTime(message = "{period.startTime.validStartTime}")
    @JsonProperty("start_time")
    private String startTime;

    @NotNull(message = "{period.endTime.notNull}")
    @TimeValid(message = "{period.endTime.valid}")
    @ValidAvailabilityEndTime(message = "{period.endTime.validEndTime}")
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
