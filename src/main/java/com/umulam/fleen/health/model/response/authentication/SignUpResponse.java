package com.umulam.fleen.health.model.response.authentication;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.authentication.AuthenticationStatus;
import com.umulam.fleen.health.constant.authentication.VerificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignUpResponse {

  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("refresh_token")
  private String refreshToken;

  @JsonProperty("authentication_status")
  private AuthenticationStatus authenticationStatus;

  @JsonProperty("verification_type")
  private VerificationType verificationType;

  @JsonProperty("phone_number")
  private String phoneNumber;

  @JsonProperty("email_address")
  private String emailAddress;
}
 
 
