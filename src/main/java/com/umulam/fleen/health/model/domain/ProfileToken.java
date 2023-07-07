package com.umulam.fleen.health.model.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "profile_token")
public class ProfileToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "reset_password_token", length = 500)
  private String resetPasswordToken;

  @Column(name = "reset_password_token_expiry_date")
  @Temporal(TemporalType.DATE)
  private Date resetPasswordTokenExpiryDate;

  @OneToOne(targetEntity = Member.class, fetch = FetchType.EAGER)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  @CreationTimestamp
  @Column(name = "created_on", updatable = false)
  private LocalDateTime createdOn;

  @UpdateTimestamp
  @Column(name = "updated_on")
  private LocalDateTime updatedOn;

}
