package com.umulam.fleen.health.event;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RescheduleSessionMeetingEvent {

  private LocalDateTime startDate;
  private LocalDateTime endDate;

  private String timezone;
  private String meetingEventId;
}
