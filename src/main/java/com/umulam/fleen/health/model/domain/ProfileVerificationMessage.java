package com.umulam.fleen.health.model.domain;

import com.umulam.fleen.health.constant.verification.ProfileVerificationMessageType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "profile_verification_message")
public class ProfileVerificationMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "title", nullable = false, length = 300)
  private String title;

  @Column(name = "verification_message_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private ProfileVerificationMessageType verificationMessageType;

  @Column(name = "message", nullable = false, columnDefinition = "TEXT")
  private String message;

  @Column(name = "html_message", nullable = false, columnDefinition = "TEXT")
  private String htmlMessage;

  @Column(name = "plain_text", nullable = false, columnDefinition = "TEXT")
  private String plainText;

  @CreationTimestamp
  @Column(name = "created_on", updatable = false)
  private LocalDateTime createdOn;

  @UpdateTimestamp
  @Column(name = "updated_on")
  private LocalDateTime updatedOn;
}
