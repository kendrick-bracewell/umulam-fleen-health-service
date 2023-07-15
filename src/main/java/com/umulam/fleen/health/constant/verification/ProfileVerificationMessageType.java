package com.umulam.fleen.health.constant.verification;

import lombok.Getter;

@Getter
public enum ProfileVerificationMessageType {

  PENDING("Pending"),
  IN_PROGRESS("In Progress"),
  DISAPPROVED("Disapproved"),
  APPROVED("Approved"),
  SIGNUP_COMPLETE("Sign Up Complete");

  private final String value;

  ProfileVerificationMessageType(String value) {
    this.value = value;
  }
}
