package com.umulam.fleen.health.model.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

import static com.umulam.fleen.health.util.DateFormatUtil.DATE;
import static com.umulam.fleen.health.util.DateFormatUtil.DATE_TIME_FORMAT;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberView extends FleenHealthView {

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

  @JsonProperty("mfa_enabled")
  private boolean mfaEnabled;

  @JsonProperty("mfa_type")
  private String mfaType;

  @JsonProperty("profile_verification_status")
  private String profileVerificationStatus;

  @JsonProperty("user_type")
  private String userType;

  @JsonProperty("gender")
  private String gender;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE)
  @JsonProperty("date_of_birth")
  private LocalDateTime dateOfBirth;

  @JsonProperty("email_address_verified")
  private boolean emailAddressVerified;

  @JsonProperty("phone_number_verified")
  private boolean phoneNumberVerified;

  @JsonProperty("member_status")
  private MemberStatusView memberStatus;

}
