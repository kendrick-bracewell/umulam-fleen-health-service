package com.umulam.fleen.health.constant.verification;

import lombok.Getter;

@Getter
public enum ProfileVerificationStatus {

  PENDING("Pending"),
  IN_PROGRESS("In Progress"),
  DISAPPROVED("Disapproved"),
  APPROVED("Approved");

  private final String value;

  ProfileVerificationStatus(String value) {
    this.value = value;
  }
}
 
 
