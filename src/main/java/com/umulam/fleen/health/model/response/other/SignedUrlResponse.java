package com.umulam.fleen.health.model.response.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignedUrlResponse {

  @JsonProperty("signed_url")
  private String signedUrl;
}
