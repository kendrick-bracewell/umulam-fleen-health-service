package com.umulam.fleen.health.event;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CancelSessionMeetingEvent {

  private String eventIdOrReference;
  private String otherEventReference;
  private String sessionReference;
}
