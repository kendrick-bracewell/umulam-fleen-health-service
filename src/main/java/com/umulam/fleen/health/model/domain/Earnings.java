package com.umulam.fleen.health.model.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "earnings")
public class Earnings {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "total_earnings", nullable = false)
  private BigDecimal totalEarnings;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  @CreationTimestamp
  @Column(name = "created_on", updatable = false)
  private LocalDateTime createdOn;

  @UpdateTimestamp
  @Column(name = "updated_on")
  private LocalDateTime updatedOn;
}
