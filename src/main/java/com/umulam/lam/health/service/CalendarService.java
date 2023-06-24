package com.umulam.lam.health.service;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class CalendarService {

  private final Calendar calendar;

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
              .list("cb21a113b9a08411d423e469e091858b3e26bd775a156d5015f6824767f9ee21@group.calendar.google.com")
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

  public void listEvent1() {
    try {

      DateTime now = new DateTime(System.currentTimeMillis());
      Events events = calendar.events()
              .list("primary")
              .setCalendarId("primary")
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
}
