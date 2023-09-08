package com.umulam.fleen.health.model.response.professional;

import com.umulam.fleen.health.model.view.base.EnumView;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetProfessionalUpdateVerificationDetailResponse {

  private String title;
  private Integer yearsOfExperience;
  private String areaOfExperience;
  private String professionalType;
  private String languagesSpoken;
  private String qualificationType;
  private String country;

  private List<?> countries;
  private List<? extends EnumView> professionalTitles;
}
