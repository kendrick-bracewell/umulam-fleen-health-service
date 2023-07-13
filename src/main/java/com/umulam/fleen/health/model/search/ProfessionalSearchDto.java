package com.umulam.fleen.health.model.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.member.ProfessionalQualificationType;
import com.umulam.fleen.health.constant.member.ProfessionalType;
import com.umulam.fleen.health.constant.professional.ProfessionalAvailabilityStatus;
import com.umulam.fleen.health.validator.EnumValid;
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

  @EnumValid(enumClass = ProfessionalAvailabilityStatus.class, message = "{professional.availabilityStatus}")
  @JsonProperty("availability_status")
  private ProfessionalAvailabilityStatus availabilityStatus;

  @EnumValid(enumClass = ProfessionalType.class, message = "{professional.type}")
  @JsonProperty("professional_type")
  private ProfessionalType professionalType;

  @EnumValid(enumClass = ProfessionalQualificationType.class, message = "{professional.qualificationType}")
  @JsonProperty("qualification")
  private ProfessionalQualificationType qualificationType;

  @JsonProperty("language_spoken")
  private String languageSpoken;

}
