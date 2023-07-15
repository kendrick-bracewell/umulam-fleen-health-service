package com.umulam.fleen.health.constant.authentication;

import lombok.Getter;

@Getter
public enum RoleType {

  SUPER_ADMINISTRATOR("SuperAdministrator"),
  ADMINISTRATOR("Administrator"),
  PROFESSIONAL("Professional"),
  REFRESH_TOKEN_USER("RefreshTokenUser"),
  PRE_VERIFIED_USER("PreVerifiedUser"),
  PRE_VERIFIED_PROFESSIONAL("PreVerifiedProfessional"),
  PRE_VERIFIED_BUSINESS("PreVerifiedBusiness"),
  PRE_APPROVED_PROFESSIONAL("PreApprovedProfessional"),
  PRE_APPROVED_BUSINESS("PreApprovedBusiness"),
  PRE_AUTHENTICATED_USER("PreAuthenticatedUser"),
  USER("User"),
  BUSINESS("Business"),
  EMPLOYEE("Employee"),
  PRE_AUTH("PreAuth"),
  PRE_OTP_AUTHENTICATED_USER("PreOtpAuthenticatedUser"),
  RESET_PASSWORD_USER("ResetPasswordUser"),
  PRE_ONBOARDED("PreOnboarded");

  private final String value;

  RoleType(String value) {
        this.value = value;
    }
}
