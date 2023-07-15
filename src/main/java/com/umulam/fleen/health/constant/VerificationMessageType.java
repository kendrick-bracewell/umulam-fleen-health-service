package com.umulam.fleen.health.constant;

import lombok.Getter;

@Getter
public enum VerificationMessageType {

  PROFILE_UPDATE,
  PRE_VERIFICATION,
  FORGOT_PASSWORD,
  PRE_AUTHENTICATION,
  MFA_SETUP,
  PRE_ONBOARDING;
}
