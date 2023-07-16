package com.umulam.fleen.health.model.request.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.member.ProfessionalQualificationType;
import com.umulam.fleen.health.constant.member.ProfessionalType;
import com.umulam.fleen.health.constant.professional.ProfessionalAvailabilityStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalSearchRequest extends MemberSearchRequest {

  @JsonProperty("availability_status")
  private ProfessionalAvailabilityStatus availabilityStatus;

  @JsonProperty("professional_type")
  private ProfessionalType professionalType;

  @JsonProperty("qualification")
  private ProfessionalQualificationType qualificationType;

  @JsonProperty("language_spoken")
  private String languageSpoken;
}
