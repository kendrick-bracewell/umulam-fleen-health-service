package com.umulam.fleen.health.service.impl;

import com.google.api.services.calendar.model.Event;
import com.umulam.fleen.health.event.CancelSessionMeetingEvent;
import com.umulam.fleen.health.event.CreateSessionMeetingEvent;
import com.umulam.fleen.health.model.domain.HealthSession;
import com.umulam.fleen.health.repository.jpa.HealthSessionJpaRepository;
import com.umulam.fleen.health.service.external.google.CalendarService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Optional;

@Component
public class FleenHealthEventListenerService {

  private final CalendarService calendarService;
  private final HealthSessionJpaRepository healthSessionRepository;

  public FleenHealthEventListenerService(CalendarService calendarService,
                                         HealthSessionJpaRepository healthSessionRepository) {
    this.calendarService = calendarService;
    this.healthSessionRepository = healthSessionRepository;
  }

  @TransactionalEventListener
  public void createMeetingSession(CreateSessionMeetingEvent meetingEvent) {
    Event event = calendarService.createEvent(meetingEvent.getStartDate(), meetingEvent.getEndDate(), meetingEvent.getAttendees(), meetingEvent.getMetadata());
    Optional<HealthSession> healthSessionExist = healthSessionRepository.findByReference(meetingEvent.getSessionReference());
    if (healthSessionExist.isPresent()) {
      HealthSession healthSession = healthSessionExist.get();
      healthSession.setEventReferenceOrId(event.getId());
      healthSession.setOtherEventReference(event.getICalUID());
      healthSession.setMeetingUrl(event.getHangoutLink());
      healthSession.setEventLink(event.getHtmlLink());
      healthSessionRepository.save(healthSession);
    }
  }

  @TransactionalEventListener
  public void cancelMeetingSession(CancelSessionMeetingEvent meetingEvent) {
    calendarService.cancelEvent(meetingEvent.getEventIdOrReference());
  }
}
