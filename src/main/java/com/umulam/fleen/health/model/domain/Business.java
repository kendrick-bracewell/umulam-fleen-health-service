package com.umulam.fleen.health.model.domain;


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
@Table(name = "business")
public class Business {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "name", nullable = false, length = 500)
  private String name;

  @Column(name = "description", length = 2500)
  private String description;

  @Column(name = "contact_address", nullable = false, length = 500)
  private String contactAddress;

  @Column(name = "registration_number_or_id", nullable = false)
  private String registrationNumberOrId;

  @Column(name = "city", nullable = false, length = 200)
  private String city;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "country_id", referencedColumnName = "id")
  private Country country;

  @Column(name = "website_link", length = 250)
  private String websiteLink;

  @OneToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", referencedColumnName = "id")
  private Member member;

  @CreationTimestamp
  @Column(name = "created_on")
  private LocalDateTime createdOn;

  @UpdateTimestamp
  @Column(name = "updated_on")
  private LocalDateTime updatedOn;

}
