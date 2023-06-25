package com.umulam.fleen.health.model.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "member", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email_address", "phone_number"})
})
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "first_name", nullable = false, length = 100)
  private String firstName;

  @Column(name = "last_name", nullable = false, length = 100)
  private String lastName;

  @Column(name = "email_address", nullable = false, length = 150)
  private String emailAddress;

  @Column(name = "phone_number", nullable = false, length = 150)
  private String phoneNumber;

  @Column(name = "password_hash", nullable = false, length = 500)
  private String password;

  @Column(name = "reset_password_token", length = 500)
  private String resetPasswordToken;

  @Column(name = "profile_photo", length = 500)
  private String profilePhoto;

  @Builder.Default
  @Column(name ="email_address_verified")
  private Boolean emailAddressVerified = false;

  @Builder.Default
  @Column(name ="phone_number_verified")
  private Boolean phoneNumberVerified = false;

  @Builder.Default
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "member_role",
    joinColumns = {
          @JoinColumn(name = "member_id")
    },
    inverseJoinColumns = {
          @JoinColumn(name = "role_id")
  })
  private Set<Role> roles = new HashSet<>();

  @CreationTimestamp
  @Column(name = "created_on")
  private LocalDateTime createdOn;

  @UpdateTimestamp
  @Column(name = "updated_on")
  private LocalDateTime updatedOn;

  public void addRole(Role role) {
    this.roles.add(role);
  }
}
