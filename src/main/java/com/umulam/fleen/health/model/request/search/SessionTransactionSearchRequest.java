package com.umulam.fleen.health.model.request.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SessionTransactionSearchRequest {

  @JsonProperty("session_reference")
  private String sessionReference;
}
