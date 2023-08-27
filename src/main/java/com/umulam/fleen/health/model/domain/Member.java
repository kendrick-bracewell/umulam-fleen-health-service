package com.umulam.fleen.health.model.domain;

import com.umulam.fleen.health.constant.authentication.MfaType;
import com.umulam.fleen.health.constant.base.ProfileType;
import com.umulam.fleen.health.constant.member.MemberGender;
import com.umulam.fleen.health.constant.verification.ProfileVerificationStatus;
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
        @UniqueConstraint(columnNames = {"email_address"}),
        @UniqueConstraint(columnNames = {"phone_number"})
})
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "first_name", nullable = false, length = 100)
  private String firstName;

  @Column(name = "last_name", nullable = false, length = 100)
  private String lastName;

  @Column(name = "email_address", nullable = false, length = 150)
  private String emailAddress;

  @Column(name = "phone_number", nullable = false, length = 15)
  private String phoneNumber;

  @Column(name = "password_hash", nullable = false, length = 500)
  private String password;

  @Column(name = "profile_photo", length = 500)
  private String profilePhoto;

  @Column(name = "date_of_birth", nullable = false)
  private LocalDateTime dateOfBirth;

  @Builder.Default
  @Column(name ="email_address_verified")
  private boolean emailAddressVerified = false;

  @Builder.Default
  @Column(name ="phone_number_verified")
  private boolean phoneNumberVerified = false;

  @Column(name = "gender", nullable = false)
  @Enumerated(EnumType.STRING)
  private MemberGender gender;

  @Builder.Default
  @Column(name = "mfa_enabled")
  private boolean mfaEnabled = false;

  @Column(name = "mfa_secret")
  private String mfaSecret;

  @Column(name = "address", length = 500)
  private String address;

  @Builder.Default
  @Column(name = "mfa_type")
  @Enumerated(EnumType.STRING)
  private MfaType mfaType = MfaType.NONE;

  @Column(name = "verification_status")
  @Enumerated(EnumType.STRING)
  private ProfileVerificationStatus verificationStatus;

  @Column(name = "user_type")
  @Enumerated(EnumType.STRING)
  private ProfileType userType;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "member_status_id")
  private MemberStatus memberStatus;

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
  @Column(name = "created_on", updatable = false)
  private LocalDateTime createdOn;

  @UpdateTimestamp
  @Column(name = "updated_on")
  private LocalDateTime updatedOn;

  public void addRole(Role role) {
    this.roles.add(role);
  }
}
