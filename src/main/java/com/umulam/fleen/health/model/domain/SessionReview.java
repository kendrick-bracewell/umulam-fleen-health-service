package com.umulam.fleen.health.model.domain;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

public class SessionReview {

  @Column
  private String review;

  @ManyToOne
  @Column(name = "patient")
  private Member patient;

  @ManyToOne
  @Column(name = "professional")
  private Member professional;

  private Integer rating;

  @Column(name = "created_on")
  private LocalDateTime createdOn;
}
