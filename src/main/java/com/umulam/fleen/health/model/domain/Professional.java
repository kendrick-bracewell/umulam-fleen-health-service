package com.umulam.fleen.health.model.domain;

import com.umulam.fleen.health.constant.professional.ProfessionalAvailabilityStatus;
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
@Table(name = "professional")
public class Professional {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "title")
  private String title;

  @Column(name = "years_of_experience", nullable = false)
  private Integer yearsOfExperience;

  @Column(name = "area_of_expertise", nullable = false, length = 2500)
  private String areaOfExpertise;

  @Column
  private String languagesSpoken;

  @Column
  @Enumerated(EnumType.STRING)
  private ProfessionalAvailabilityStatus availabilityStatus;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "country_id", referencedColumnName = "id")
  private Country country;

  @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "member_id", referencedColumnName = "id")
  private Member member;

  @CreationTimestamp
  @Column(name = "created_on", updatable = false)
  private LocalDateTime createdOn;

  @UpdateTimestamp
  @Column(name = "updated_on")
  private LocalDateTime updatedOn;
}
