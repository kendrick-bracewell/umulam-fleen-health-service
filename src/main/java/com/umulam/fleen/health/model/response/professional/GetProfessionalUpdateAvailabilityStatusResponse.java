package com.umulam.fleen.health.model.response.professional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.professional.ProfessionalAvailabilityStatus;

public interface GetProfessionalUpdateAvailabilityStatusResponse {

  @JsonProperty("availability_status")
  ProfessionalAvailabilityStatus getAvailabilityStatus();
}
