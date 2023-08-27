package com.umulam.fleen.health.model.domain;

import com.umulam.fleen.health.constant.verification.VerificationDocumentType;
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
@Table(name = "verification_document")
public class VerificationDocument {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "document_type")
  @Enumerated(EnumType.STRING)
  private VerificationDocumentType verificationDocumentType;

  @Column(name = "filename", nullable = false, length = 500)
  private String filename;

  @Column(name = "link", nullable = false, length = 500)
  private String link;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", referencedColumnName = "id", nullable = false)
  private Member member;

  @CreationTimestamp
  @Column(name = "created_on")
  private LocalDateTime createdOn;

  @UpdateTimestamp
  @Column(name = "updated_on")
  private LocalDateTime updatedOn;
}
