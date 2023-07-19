package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.event.CreateSessionMeetingEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class FleenHealthEventService {

  private final ApplicationEventPublisher eventPublisher;

  public FleenHealthEventService(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  public void publishCreateSession(CreateSessionMeetingEvent event) {
    eventPublisher.publishEvent(event);
  }
}
