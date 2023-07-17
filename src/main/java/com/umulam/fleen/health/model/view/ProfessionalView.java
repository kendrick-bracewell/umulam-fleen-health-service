package com.umulam.fleen.health.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalView extends FleenHealthView {

  private String title;

  @JsonProperty("type")
  private String professionalType;

  @JsonProperty("qualification")
  private String qualification;

  @JsonProperty("years_of_experience")
  private Integer yearsOfExperience;

  @JsonProperty("area_of_expertise")
  private String areaOfExpertise;

  @JsonProperty("availability_status")
  private String availabilityStatus;

  @JsonProperty("languages_spoken")
  private String languagesSpoken;

  private MemberView member;
  private CountryView country;

  @JsonProperty("verification_documents")
  private List<VerificationDocumentView> verificationDocuments;
}
