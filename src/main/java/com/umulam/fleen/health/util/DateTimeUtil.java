package com.umulam.fleen.health.util;

import lombok.extern.slf4j.Slf4j;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.umulam.fleen.health.util.DateFormatUtil.*;
import static java.util.Objects.nonNull;

@Slf4j
public class DateTimeUtil {
  public static Date asDate(LocalDate localDate) {
    return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
  }

  public static Date asDate(LocalDateTime localDateTime) {
    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
  }

  public static LocalDate asLocalDate(Date date) {
    return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
  }

  public static LocalDateTime asLocalDateTime(Date date) {
    return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  public static long toMilliseconds(LocalDateTime dateTime, String timezone) {
    if (dateTime != null) {
      ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.of(timezone));
      return zonedDateTime.toInstant().toEpochMilli();
    }
    return 0;
  }

  public static long toHours(Date date1, Date date2) {
    long diffInMillis = Math.abs(date2.getTime() - date1.getTime());
    return TimeUnit.HOURS.convert(diffInMillis, TimeUnit.MILLISECONDS);
  }

  public static long toMilliseconds(LocalDateTime dateTime) {
    if (dateTime != null) {
      return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
    return 0;
  }

  public static Long getTimeInMillis(int seconds, int minutes, int hours, int days) {
    List<Integer> times = List.of(1000, seconds, minutes, hours, days);
    return times
            .stream()
            .filter(time -> time > 0)
            .reduce(1, Math::multiplyExact)
            .longValue();
  }

  public static LocalDateTime addHoursFromNow(int hour) {
    return LocalDateTime.now().plusHours(hour);
  }

  public static LocalDateTime addMinutesFromNow(int minute) {
    return LocalDateTime.now().plusMinutes(minute);
  }

  public static LocalDateTime toLocalDateTime(String date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE);
    return LocalDate.parse(date, formatter).atStartOfDay();
  }

  public static LocalDateTime getDefaultDateOfBirth() {
    return LocalDateTime.of(2000, 1, 1, 0, 0, 0);
  }

  public static LocalTime getWorkingHourStart() {
    return LocalTime.of(9, 0);
  }

  public static LocalTime getWorkingHourEnd() {
    return LocalTime.of(18, 0);
  }

  public static boolean validateWorkingHour(String time) {
    if (nonNull(time)) {
      try {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(TIME);
        LocalTime workingTime = LocalTime.parse(time, dtf);
        return !workingTime.isBefore(getWorkingHourStart()) && !workingTime.isAfter(getWorkingHourEnd());
      } catch (DateTimeParseException ex) {
        log.error(ex.getMessage(), ex);
      }
    }
    return false;
  }

  public static LocalTime toTime(String time) {
    if (nonNull(time)) {
      try {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(TIME);
        return LocalTime.parse(time, dtf);
      } catch (DateTimeParseException ex) {
        log.error(ex.getMessage(), ex);
      }
    }
    return null;
  }

  public static boolean isDateOrTimeValid(String dateTime, String pattern) {
    if (nonNull(dateTime)) {
      try {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        dtf.parse(dateTime);
        return true;
      } catch (DateTimeParseException ex) {
        log.error(ex.getMessage(), ex);
      }
    }
    return false;
  }

  public static LocalDate toDate(String date) {
    if (nonNull(date)) {
      try {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE);
        return LocalDate.parse(date, dtf);
      } catch (DateTimeParseException ex) {
        log.error(ex.getMessage(), ex);
      }
    }
    return null;
  }

  public static String asDateTimeWithNoSeconds(LocalDateTime dateTime) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_NO_SECONDS);
    if (nonNull(dateTime)) {
      return dateTime.format(formatter);
    }
    return LocalDateTime.now().format(formatter);
  }
}
