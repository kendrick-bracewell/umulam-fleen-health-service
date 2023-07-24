package com.umulam.fleen.health.model.domain;

import com.umulam.fleen.health.constant.session.HealthSessionStatus;
import com.umulam.fleen.health.constant.session.SessionLocation;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.TimeZone;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "health_session", indexes = {
  @Index(columnList = "reference", name = "hs_ref_index", unique = true)
})
public class HealthSession {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "reference", nullable = false)
  private String reference;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "patient_id")
  private Member patient;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "professional_id")
  private Member professional;

  @Column(name = "comment", length = 1000)
  private String comment;

  @Column(name = "notes", length = 1000)
  private String note;

  @Column(name = "date", nullable = false)
  private LocalDate date;

  @Column(name = "time", nullable = false)
  private LocalTime time;

  @Column(name = "timezone")
  private String timeZone;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private HealthSessionStatus status;

  @Column(name = "location", nullable = false)
  @Enumerated(EnumType.STRING)
  private SessionLocation location;

  @Column(name = "document_link")
  private String documentLink;

  @Column(name = "event_reference_or_id")
  private String eventReferenceOrId;

  @Column(name = "other_event_reference")
  private String otherEventReference;

  @Column(name = "meeting_url")
  private String meetingUrl;

  @Column(name = "event_link")
  private String eventLink;

  @CreationTimestamp
  @Column(name = "created_on", updatable = false)
  private LocalDateTime createdOn;

  @UpdateTimestamp
  @Column(name = "updated_on")
  private LocalDateTime updatedOn;
}
