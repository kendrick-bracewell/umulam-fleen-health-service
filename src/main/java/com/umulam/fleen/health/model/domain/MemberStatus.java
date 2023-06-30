package com.umulam.fleen.health.model.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member_status")
public class MemberStatus {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "title", nullable = false, length = 200)
  private String title;

  @Column(name = "code", nullable = false, length = 200)
  private String code;

  @Column(name = "description", length = 500)
  private String description;

  @CreationTimestamp
  @Column(name = "created_on")
  private LocalDateTime createdOn;

  @UpdateTimestamp
  @Column(name = "updated_on")
  private LocalDateTime updatedOn;
}
