package com.umulam.fleen.health.event;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSessionMeetingEvent {

  private LocalDate date;
  private LocalTime time;

  private String timezone;
  private List<String> attendees;
}
