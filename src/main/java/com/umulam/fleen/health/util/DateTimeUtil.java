package com.umulam.fleen.health.util;

import java.time.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
    return (long) 1000 * seconds * minutes * hours * days;
  }
}
