package com.umulam.fleen.health.model.response.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.authentication.MfaType;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InitMfaResponse {

  @JsonProperty("mfa_type")
  private MfaType mfaType;
}
