package com.umulam.fleen.health.constant.member;

import com.umulam.fleen.health.adapter.ApiParameter;
import lombok.Getter;

@Getter
public enum ProfessionalQualificationType {

  DIPLOMA("Diploma"),
  BACHELOR("Bachelor"),
  MASTER("Master"),
  DOCTORATE("Doctorate");

  private final String value;

  ProfessionalQualificationType(String value) {
    this.value = value;
  }
}
