package com.umulam.fleen.health.model.request.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.verification.ProfileVerificationMessageType;
import com.umulam.fleen.health.model.request.search.base.SearchRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class ProfileVerificationMessageSearchRequest extends SearchRequest {

  @JsonProperty("verification_message_type")
  private ProfileVerificationMessageType verificationMessageType;
}
