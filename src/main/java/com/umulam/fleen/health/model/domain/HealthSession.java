package com.umulam.fleen.health.model.domain;

import com.umulam.fleen.health.constant.session.HealthSessionStatus;
import com.umulam.fleen.health.constant.session.SessionLocation;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.TimeZone;

public class HealthSession {

  @Column(name = "id")
  private Integer id;

  @Column
  private String reference;

  @ManyToOne
  @JoinColumn(name = "patient")
  private Member patient;

  @ManyToOne
  @JoinColumn(name = "professional")
  private Member professional;

  @Column
  private String description;

  @Column(name = "date")
  private LocalDate date;

  @Column(name = "time")
  private LocalTime time;

  @Column(name = "timezone")
  private TimeZone timeZone;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private HealthSessionStatus status;

  @Column(name = "location")
  @Enumerated(EnumType.STRING)
  private SessionLocation location;

  private String documentLink;

  private String documentLink2;
}
