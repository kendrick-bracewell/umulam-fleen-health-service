package com.umulam.fleen.health.model.view.professional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.model.view.country.CountryView;
import com.umulam.fleen.health.model.view.VerificationDocumentView;
import com.umulam.fleen.health.model.view.base.FleenHealthView;
import com.umulam.fleen.health.model.view.member.MemberView;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
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

  @JsonProperty("price")
  private Double price;

  private MemberView member;
  private CountryView country;

  @JsonProperty("verification_documents")
  private List<VerificationDocumentView> verificationDocuments;

}
 
