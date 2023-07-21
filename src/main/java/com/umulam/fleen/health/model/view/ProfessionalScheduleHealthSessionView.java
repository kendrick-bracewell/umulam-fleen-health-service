package com.umulam.fleen.health.model.view;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalScheduleHealthSessionView {

  private LocalDate date;
  private LocalTime time;
  private String timezone;
}
