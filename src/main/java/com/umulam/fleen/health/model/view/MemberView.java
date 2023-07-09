package com.umulam.fleen.health.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberView {

  private Integer id;

  @JsonProperty("first_name")
  private String firstName;

  @JsonProperty("last_name")
  private String lastName;

  @JsonProperty("email_address")
  private String emailAddress;

  @JsonProperty("phone_number")
  private String phoneNumber;

  @JsonProperty("profile_photo")
  private String profilePhoto;

  @JsonProperty("email_address_verified")
  private boolean emailAddressVerified;

  @JsonProperty("phone_number_verified")
  private boolean phoneNumberVerified;

  @JsonProperty("mfa_enabled")
  private boolean mfaEnabled;

  @JsonProperty("mfa_type")
  private String mfaType;

  @JsonProperty("profile_verification_status")
  private String profileVerificationStatus;

  @JsonProperty("user_type")
  private String userType;

  @JsonProperty("member_status")
  private MemberStatusView memberStatus;
}
