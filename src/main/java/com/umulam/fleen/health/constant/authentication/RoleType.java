package com.umulam.fleen.health.constant.authentication;

import lombok.Getter;

@Getter
public enum RoleType {

  SUPER_ADMINISTRATOR("SuperAdministrator"),
  ADMINISTRATOR("Administrator"),
  PROFESSIONAL("Professional"),
  REFRESH_TOKEN("RefreshToken"),
  PRE_VERIFIED_USER("PreVerifiedUser"),
  PRE_AUTHENTICATED_USER("PreAuthenticatedUser"),
  USER("User"),
  PRE_AUTH("PreAuth"),
  PRE_OTP_AUTHENTICATED_USER("PreOtpAuthenticatedUser");

  private final String value;

  RoleType(String value) {
        this.value = value;
    }
}
