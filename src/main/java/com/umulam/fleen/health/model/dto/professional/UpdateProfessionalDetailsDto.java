package com.umulam.fleen.health.model.dto.professional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.member.ProfessionalQualificationType;
import com.umulam.fleen.health.constant.member.ProfessionalType;
import com.umulam.fleen.health.model.domain.Country;
import com.umulam.fleen.health.model.domain.Professional;
import com.umulam.fleen.health.validator.CountryExists;
import com.umulam.fleen.health.validator.EnumValid;
import com.umulam.fleen.health.validator.IsNumber;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfessionalDetailsDto {

  @NotBlank(message = "{professional.title.notEmpty}")
  @Size(min = 1, max = 500, message = "{professional.title.size}")
  private String title;

  @NotNull(message = "{professional.yearsOfExperience.notNull}")
  @IsNumber(message = "{professional.yearsOfExperience.isNumber}")
  @Size(min = 1, max = 2, message = "{professional.yearsOfExperience.size}")
  @JsonProperty("years_of_experience")
  private String yearsOfExperience;

  @NotNull(message = "{professional.areaOfExpertise.notEmpty}")
  @Size(max = 2500, message = "{professional.areaOfExpertise.size}")
  @JsonProperty("area_of_expertise")
  private String areaOfExpertise;

  @NotBlank(message = "{professional.country.notEmpty}")
  @IsNumber(message = "{professional.country.isNumber}")
  @CountryExists(message = "{professional.country.exists}")
  private String country;

  @NotBlank(message = "{professional.languagesSpoken.notEmpty}")
  @Size(min = 1, max = 150, message = "{professional.languagesSpoken.size}")
  @JsonProperty("languages_spoken")
  private String languagesSpoken;

  @EnumValid(enumClass = ProfessionalType.class, message = "{professional.type}")
  @JsonProperty("professional_type")
  private String professionalType;

  @EnumValid(enumClass = ProfessionalQualificationType.class, message = "{professional.qualificationType}")
  @JsonProperty("qualification_type")
  private String qualificationType;

  public Professional toProfessional() {
    return Professional.builder()
            .title(title)
            .yearsOfExperience(Integer.parseInt(yearsOfExperience))
            .areaOfExpertise(areaOfExpertise)
            .country(Country.builder()
                    .id(Integer.parseInt(country)).build())
            .languagesSpoken(languagesSpoken)
            .professionalType(ProfessionalType.valueOf(professionalType))
            .qualificationType(ProfessionalQualificationType.valueOf(qualificationType))
            .build();
  }
}
