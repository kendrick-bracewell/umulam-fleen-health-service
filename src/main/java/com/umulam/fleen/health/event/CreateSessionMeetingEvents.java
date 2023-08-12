package com.umulam.fleen.health.event;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSessionMeetingEvents {

  List<CreateSessionMeetingEvent> meetingEvents;
}
