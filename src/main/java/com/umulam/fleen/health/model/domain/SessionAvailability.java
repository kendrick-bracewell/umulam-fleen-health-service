package com.umulam.fleen.health.model.domain;

import com.umulam.fleen.health.constant.session.TimePeriod;

public class SessionAvailability {

  private Integer id;
  private String dayOfWeek;
  private String startTime;
  private String endTime;
  private String timezone;
  private TimePeriod period;
  private Boolean available;
  private String reasonForUnavailability;
}
