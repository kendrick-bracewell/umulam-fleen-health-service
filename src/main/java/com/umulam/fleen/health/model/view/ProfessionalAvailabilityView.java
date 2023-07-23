package com.umulam.fleen.health.model.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.model.view.base.FleenHealthView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.umulam.fleen.health.util.DateFormatUtil.TIME;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class ProfessionalAvailabilityView extends FleenHealthView {

  @JsonProperty("day_of_the_week")
  private String dayOfTheWeek;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TIME)
  @JsonProperty("start_time")
  private LocalTime startTime;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TIME)
  @JsonProperty("end_time")
  private LocalTime endTime;
}
