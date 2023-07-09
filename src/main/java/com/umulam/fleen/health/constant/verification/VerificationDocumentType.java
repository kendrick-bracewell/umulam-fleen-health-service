package com.umulam.fleen.health.constant.verification;

import lombok.Getter;

@Getter
public enum VerificationDocumentType {

  // FOR BUSINESS

  // REGISTRATION
  BUSINESS_REGISTRATION_DOCUMENT("Business Registration Document"),
  BUSINESS_TAX_IDENTIFICATION("Business Tax Identification Document"),

  // IDENTIFICATION
  PASSPORT("Passport"),
  DRIVER_LICENSE("Driver License"),
  NATIONAL_ID_CARD("National Identity Card"),

  // PROOF OF ADDRESS
  LEASE_AGREEMENT("Lease Agreement"),
  UTILITY_BILL("Utility Bill"),

  // FOR PROFESSIONAL
  // LICENSE
  PROFESSIONAL_LICENSE("Professional License"),

  // EDUCATION
  EDUCATION_CERTIFICATE("Education Certificate"),
  TRANSCRIPT("Transcript"),

  // MEMBERSHIP
  PROFESSIONAL_MEMBERSHIP("Professional Membership"),

  // OTHER
  CURRICULUM_VITAE("Curriculum Vitae");

  private final String value;

  VerificationDocumentType(String value) {
    this.value = value;
  }
}
