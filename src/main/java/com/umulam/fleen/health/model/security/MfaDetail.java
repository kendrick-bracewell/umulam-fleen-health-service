package com.umulam.fleen.health.model.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MfaDetail {

  @JsonProperty("qr_code")
  private String qrCode;
  private String secret;
  private boolean enabled;

  @JsonProperty("mfa_type")
  private String mfaType;
}
