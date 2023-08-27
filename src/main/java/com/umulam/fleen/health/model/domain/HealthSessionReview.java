package com.umulam.fleen.health.model.domain;

import com.umulam.fleen.health.constant.session.HealthSessionReviewRating;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "health_session_review")
public class HealthSessionReview {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "review", nullable = false, length = 1000)
  private String review;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "patient_id", nullable = false)
  private Member patient;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "professional_id", nullable = false)
  private Member professional;

  @Column(name = "rating", nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private HealthSessionReviewRating rating;

  @CreationTimestamp
  @Column(name = "created_on", updatable = false)
  private LocalDateTime createdOn;

  @ManyToOne
  @JoinColumn(name = "health_session_id", nullable = false)
  private HealthSession healthSession;
}
