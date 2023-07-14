package com.umulam.fleen.health.model.request.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.member.ProfessionalQualificationType;
import com.umulam.fleen.health.constant.member.ProfessionalType;
import com.umulam.fleen.health.constant.professional.ProfessionalAvailabilityStatus;
import com.umulam.fleen.health.constant.verification.ProfileVerificationStatus;
import com.umulam.fleen.health.model.request.search.base.SearchRequest;
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
public class ProfessionalSearchRequest extends SearchRequest {

  @JsonProperty("first_name")
  private String firstName;

  @JsonProperty("last_name")
  private String lastName;

  @JsonProperty("email_address")
  private String emailAddress;

  @JsonProperty("availability_status")
  private ProfessionalAvailabilityStatus availabilityStatus;

  @JsonProperty("professional_type")
  private ProfessionalType professionalType;

  @JsonProperty("qualification")
  private ProfessionalQualificationType qualificationType;

  @JsonProperty("language_spoken")
  private String languageSpoken;

  @JsonProperty("verification_status")
  private ProfileVerificationStatus verificationStatus;
}
