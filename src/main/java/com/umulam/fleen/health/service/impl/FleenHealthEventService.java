package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.event.CancelSessionMeetingEvent;
import com.umulam.fleen.health.event.CreateSessionMeetingEvent;
import com.umulam.fleen.health.event.CreateSessionMeetingEvents;
import com.umulam.fleen.health.event.RescheduleSessionMeetingEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FleenHealthEventService {

  private final ApplicationEventPublisher eventPublisher;

  public FleenHealthEventService(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  public void publishCreateSession(CreateSessionMeetingEvents events) {
    eventPublisher.publishEvent(events);
  }

  public void publishCancelSession(CancelSessionMeetingEvent event) {
    eventPublisher.publishEvent(event);
  }

  public void publishRescheduleSession(RescheduleSessionMeetingEvent event) {
    eventPublisher.publishEvent(event);
  }
}
