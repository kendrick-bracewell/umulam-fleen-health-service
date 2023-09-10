package com.umulam.fleen.health.constant.member;

import lombok.Getter;

@Getter
public enum ProfessionalType {
  THERAPIST("Therapist"),
  PSYCHOLOGIST("Psychologist"),
  COUNSELOR("Counselor");

  private final String value;

  ProfessionalType(String value) {
    this.value = value;
  }

}
