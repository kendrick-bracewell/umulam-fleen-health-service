package com.umulam.fleen.health.model.response.healthsession;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface GetUpdateHealthSessionNote {

  String getNote();

  @JsonProperty("professional_id")
  Integer getProfessionalId();
}
