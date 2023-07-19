package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.service.external.google.CalendarService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class FleenHealthEventListenerService {

  private final CalendarService calendarService;

  public FleenHealthEventListenerService(CalendarService calendarService) {
    this.calendarService = calendarService;
  }

  @Async
  @TransactionalEventListener
  public void createSession() {

  }
}
