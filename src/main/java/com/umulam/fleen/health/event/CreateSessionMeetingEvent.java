package com.umulam.fleen.health.event;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSessionMeetingEvent {

  private LocalDateTime startDate;
  private LocalDateTime endDate;

  private String timezone;
  private List<String> attendees;
  private Map<String, String> metadata;
  private String sessionReference;
}
