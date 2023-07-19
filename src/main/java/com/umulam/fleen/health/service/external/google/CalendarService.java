package com.umulam.fleen.health.service.external.google;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import com.umulam.fleen.health.constant.session.SessionLocation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.umulam.fleen.health.util.DateTimeUtil.asDate;
import static com.umulam.fleen.health.util.DateTimeUtil.toMilliseconds;

@Slf4j
@Component
public class CalendarService {

  private final Calendar calendar;
  private static final String CALENDAR_ID = "primary";
  private static final String EVENT_LOCATION = "Abuja, Nigeria";
  private static final String EVENT_SUMMARY = "Fleen Health Session";
  private static final String EVENT_DESCRIPTION = "Fleen Health Telehealth Session";
  private static final String EVENT_DISPLAY_NAME = "Fleen Health";
  private static final String DEFAULT_TIMEZONE = "Africa/Lagos";
  private static final String DEFAULT_CONFERENCE_NAME = "Lam Telehealth Session";

  public CalendarService(Calendar calendar) {
    this.calendar = calendar;
  }

  public void listEvent() {
    try {
      DateTime now = new DateTime(System.currentTimeMillis());
//      Date oneHourFromNow = Utils.getRelativeDate(java.util.Calendar.HOUR, 1);
//      Date twoHoursFromNow = Utils.getRelativeDate(java.util.Calendar.HOUR, 2);
//      DateTime start = new DateTime(oneHourFromNow, TimeZone.getTimeZone("UTC"));
//      DateTime end = new DateTime(twoHoursFromNow, TimeZone.getTimeZone("UTC"));
//      Event event = new Event().setSummary(summary).setReminders(new Reminders().setUseDefault(false)).setStart(new EventDateTime().setDateTime(start)).setEnd(new EventDateTime().setDateTime(end));
      Events events = calendar.events()
              .list(CALENDAR_ID)
              .setMaxResults(5)
              .setTimeMin(now)
              .setOrderBy("startTime")
              .setSingleEvents(true)
              .execute();
      List<Event> items = events.getItems();
      if (items.isEmpty()) {
        System.out.println("No upcoming events found.");
      } else {
        System.out.println("Upcoming events");
        for (Event event : items) {
          DateTime start = event.getStart().getDateTime();
          if (start == null) {
            start = event.getStart().getDate();
          }
          System.out.printf("%s (%s)\n", event.getSummary(), start);
        }
      }
    } catch (IOException ignored) {

    }
  }

  public Object createEvent(LocalDateTime startDate, LocalDateTime endDate, List<String> emailAddresses) {
    try {
      Event event = new Event();
      event.setLocation(SessionLocation.REMOTE.name());
      event.setSummary(EVENT_SUMMARY);
      event.setDescription(EVENT_DESCRIPTION);

      Event.Creator creator = new Event.Creator();
      creator.setEmail("umulam@volunux.com");
      creator.setDisplayName(EVENT_DISPLAY_NAME);
      event.setCreator(creator);
      event.setGuestsCanSeeOtherGuests(false);
      event.setVisibility("private");

      ConferenceSolution conferenceSolution = new ConferenceSolution();
      conferenceSolution.setName(DEFAULT_CONFERENCE_NAME);

      ConferenceSolutionKey conferenceSolutionKey = new ConferenceSolutionKey();
      conferenceSolutionKey.setType("hangoutsMeet");

      CreateConferenceRequest createConferenceRequest = new CreateConferenceRequest();
      createConferenceRequest.setRequestId(createConferenceRequestId(startDate, endDate));
      createConferenceRequest.setConferenceSolutionKey(conferenceSolutionKey);

      ConferenceData conferenceData = new ConferenceData();
      conferenceData.setCreateRequest(createConferenceRequest);

      conferenceData.setConferenceSolution(conferenceSolution);
      event.setConferenceData(conferenceData);

      DateTime startDateTime = new DateTime(asDate(startDate));
      EventDateTime start = new EventDateTime();
      start.setDateTime(startDateTime);
      start.setTimeZone(DEFAULT_TIMEZONE);
      event.setStart(start);

      DateTime endDateTime = new DateTime(asDate(endDate));
      EventDateTime end = new EventDateTime();
      end.setDateTime(endDateTime);
      end.setTimeZone(DEFAULT_TIMEZONE);
      event.setEnd(end);

      List<EventAttendee> attendees = new ArrayList<>();
      emailAddresses
              .stream()
              .filter(Objects::nonNull)
              .forEach(emailAddress -> {
                EventAttendee attendee = new EventAttendee();
                attendee.setEmail(emailAddress);
                attendees.add(attendee);
              });
      attendees.get(0).setOrganizer(true);
      event.setAttendees(attendees);

      Event.Reminders reminders = new Event.Reminders()
              .setUseDefault(false)
              .setOverrides(getEventReminders());
      event.setReminders(reminders);

      Calendar.Events.Insert insert = calendar.events().insert(CALENDAR_ID, event);
      insert.setConferenceDataVersion(1);
      insert.setSendUpdates("all");
      return insert.execute();
    } catch (IOException exception) {
      log.error(exception.getMessage(), exception);
      return null;
    }
  }

  private List<EventReminder> getEventReminders() {
    List<EventReminder> eventReminders = new ArrayList<>();
    eventReminders.add(new EventReminder().setMethod("email").setMinutes(24 * 60));
    eventReminders.add(new EventReminder().setMethod("popup").setMinutes(10));
    return eventReminders;
  }

  private String createConferenceRequestId(LocalDateTime startDate, LocalDateTime endDate) {
    return String.valueOf(toMilliseconds(startDate, DEFAULT_TIMEZONE))
            .concat("-")
            .concat(String.valueOf(toMilliseconds(endDate, DEFAULT_TIMEZONE)));
  }
}
