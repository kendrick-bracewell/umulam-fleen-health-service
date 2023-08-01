package com.umulam.fleen.health.service.impl;

import com.google.api.services.calendar.model.Event;
import com.umulam.fleen.health.constant.session.HealthSessionStatus;
import com.umulam.fleen.health.event.CancelSessionMeetingEvent;
import com.umulam.fleen.health.event.CreateSessionMeetingEvent;
import com.umulam.fleen.health.event.RescheduleSessionMeetingEvent;
import com.umulam.fleen.health.model.domain.HealthSession;
import com.umulam.fleen.health.repository.jpa.HealthSessionJpaRepository;
import com.umulam.fleen.health.service.external.google.CalendarService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.Optional;

import static com.umulam.fleen.health.service.external.google.CalendarService.EVENT_SUMMARY_KEY;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;
import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Service
public class FleenHealthEventListenerService {

  private final CalendarService calendarService;
  private final HealthSessionJpaRepository healthSessionRepository;

  public FleenHealthEventListenerService(CalendarService calendarService,
                                         HealthSessionJpaRepository healthSessionRepository) {
    this.calendarService = calendarService;
    this.healthSessionRepository = healthSessionRepository;
  }

  @TransactionalEventListener(phase = AFTER_COMMIT)
  @Transactional(propagation = REQUIRES_NEW)
  public void createMeetingSession(List<CreateSessionMeetingEvent> events) {
    for (CreateSessionMeetingEvent meetingEvent : events) {
      meetingEvent.getMetadata().put(EVENT_SUMMARY_KEY, getMeetingEventSummary(meetingEvent.getPatientName(), meetingEvent.getProfessionalName()));
      Event event = calendarService.createEvent(meetingEvent.getStartDate(), meetingEvent.getEndDate(), meetingEvent.getAttendees(), meetingEvent.getMetadata());
      Optional<HealthSession> healthSessionExist = healthSessionRepository.findByReference(meetingEvent.getSessionReference());
      if (healthSessionExist.isPresent()) {
        HealthSession healthSession = healthSessionExist.get();
        healthSession.setEventReferenceOrId(event.getId());
        healthSession.setOtherEventReference(event.getICalUID());
        healthSession.setMeetingUrl(event.getHangoutLink());
        healthSession.setEventLink(event.getHtmlLink());
        healthSession.setStatus(HealthSessionStatus.SCHEDULED);
        healthSessionRepository.save(healthSession);
      }
    }
  }

  @TransactionalEventListener(phase = AFTER_COMMIT)
  @Transactional(propagation = REQUIRES_NEW)
  public void cancelMeetingSession(CancelSessionMeetingEvent meetingEvent) {
    calendarService.cancelEvent(meetingEvent.getEventIdOrReference());
  }

  @TransactionalEventListener(phase = AFTER_COMMIT)
  @Transactional(propagation = REQUIRES_NEW)
  public void reScheduleMeetingEvent(RescheduleSessionMeetingEvent meetingEvent) {
    calendarService.rescheduleEvent(meetingEvent.getMeetingEventId(), meetingEvent.getStartDate(), meetingEvent.getEndDate());
  }

  private String getMeetingEventSummary(String patientName, String professionalName) {
    return String.format("Fleen Health Session, %s/%s 1-on-1", patientName, professionalName);
  }
}
