package com.umulam.fleen.health.model.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.member.ProfessionalQualificationType;
import com.umulam.fleen.health.constant.member.ProfessionalType;
import com.umulam.fleen.health.constant.professional.ProfessionalAvailabilityStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProfessionalSearchDto {

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

  @JsonProperty("created_on")
  private LocalDateTime createdOn;

  @JsonProperty("updated_on")
  private LocalDateTime updatedOn;
}
