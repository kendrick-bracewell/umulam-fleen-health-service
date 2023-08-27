package com.umulam.fleen.health.model.domain;

import com.umulam.fleen.health.constant.professional.AvailabilityDayOfTheWeek;
import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "professional_availability")
public class ProfessionalAvailability {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @Column(name = "day_of_week", nullable = false)
  @Enumerated(EnumType.STRING)
  private AvailabilityDayOfTheWeek dayOfWeek;

  @Column(name = "start_time", nullable = false)
  private LocalTime startTime;

  @Column(name = "end_time", nullable = false)
  private LocalTime endTime;

  public boolean isTimeInRange(LocalTime time) {
    return (time.equals(startTime) || time.isAfter(startTime)) && time.isBefore(endTime);
  }
}
