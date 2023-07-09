package com.umulam.fleen.health.model.view;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberView {

  private Integer id;
  private String firstName;
  private String lastName;
  private String emailAddress;
  private String phoneNumber;
  private String profilePhoto;
  private boolean emailAddressVerified;
  private boolean phoneNumberVerified;
  private boolean mfaEnabled;
  private String mfaType;
  private String profileVerificationStatus;
  private String userType;
  private MemberStatusView memberStatus;
}
