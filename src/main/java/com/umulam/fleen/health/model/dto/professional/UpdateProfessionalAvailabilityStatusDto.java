package com.umulam.fleen.health.model.dto.professional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.professional.ProfessionalAvailabilityStatus;
import com.umulam.fleen.health.validator.EnumValid;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfessionalAvailabilityStatusDto {

  @EnumValid(enumClass = ProfessionalAvailabilityStatus.class, message = "{professional.availabilityStatus}")
  @JsonProperty("availability_status")
  private String availabilityStatus;
}
