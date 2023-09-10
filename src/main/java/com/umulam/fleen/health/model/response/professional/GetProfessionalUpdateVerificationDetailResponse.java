package com.umulam.fleen.health.model.response.professional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.model.view.base.EnumView;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetProfessionalUpdateVerificationDetailResponse {

  private String title;
  private String country;

  @JsonProperty("years_of_experience")
  private Integer yearsOfExperience;

  @JsonProperty("area_of_expertise")
  private String areaOfExpertise;

  @JsonProperty("professional_type")
  private String professionalType;

  @JsonProperty("languages_spoken")
  private String languagesSpoken;

  @JsonProperty("qualification_type")
  private String qualificationType;

  private List<?> countries;

  @JsonProperty("professional_titles")
  private List<? extends EnumView> professionalTitles;
}
