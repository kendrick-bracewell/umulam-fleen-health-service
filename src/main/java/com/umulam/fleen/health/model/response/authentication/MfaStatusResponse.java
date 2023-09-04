package com.umulam.fleen.health.model.response.authentication;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MfaStatusResponse {

  private boolean enabled;

  @JsonProperty("mfa_type")
  private String mfaType;
}
