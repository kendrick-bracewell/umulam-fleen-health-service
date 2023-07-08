package com.umulam.fleen.health.model.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.authentication.MfaSetupStatus;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MfaDetail {

  private String secret;
  private boolean enabled;

  @JsonProperty("qr_code")
  private String qrCode;

  @JsonProperty("mfa_type")
  private String mfaType;

  @JsonProperty("mfa_setup_status")
  private MfaSetupStatus mfaSetupStatus;
}
