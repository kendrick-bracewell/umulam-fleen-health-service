package com.umulam.fleen.health.model.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.annotation.ToUpperCase;
import com.umulam.fleen.health.constant.member.ProfessionalQualificationType;
import com.umulam.fleen.health.constant.member.ProfessionalType;
import com.umulam.fleen.health.constant.professional.ProfessionalAvailabilityStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalSearchDto extends SearchRequest {

  @JsonProperty("first_name")
  private String firstName;

  @JsonProperty("last_name")
  private String lastName;

  @JsonProperty("email_address")
  private String emailAddress;

  @JsonProperty("availability_status")
  @ToUpperCase
  private ProfessionalAvailabilityStatus availabilityStatus;

  @JsonProperty("professional_type")
  @ToUpperCase
  private ProfessionalType professionalType;

  @JsonProperty("qualification")
  @ToUpperCase
  private ProfessionalQualificationType qualificationType;

  @JsonProperty("language_spoken")
  private String languageSpoken;

}
