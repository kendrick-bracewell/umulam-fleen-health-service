package com.umulam.fleen.health.model.domain;

import com.umulam.fleen.health.constant.verification.ProfileVerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "profile_verification_history")
public class ProfileVerificationHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @Column(name = "verification_status")
  @Enumerated(EnumType.STRING)
  private ProfileVerificationStatus profileVerificationStatus;

  @Column(name = "message", nullable = false)
  private String message;

  @Column(name = "comment")
  private String comment;

  @CreationTimestamp
  @Column(name = "created_on", updatable = false)
  private LocalDateTime createdOn;

}
