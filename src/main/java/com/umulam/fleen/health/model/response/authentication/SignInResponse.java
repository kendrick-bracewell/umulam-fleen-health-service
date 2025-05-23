package com.umulam.fleen.health.model.response.authentication;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.authentication.AuthenticationStatus;
import com.umulam.fleen.health.constant.authentication.MfaType;
import com.umulam.fleen.health.constant.authentication.NextAuthentication;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignInResponse {

  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("refresh_token")
  private String refreshToken;

  @JsonProperty("email_address")
  private String emailAddress;

  @JsonProperty("phone_number")
  private String phoneNumber;

  @JsonProperty("authentication_status")
  private AuthenticationStatus authenticationStatus;

  @JsonProperty("mfa_type")
  private MfaType mfaType;

  @JsonProperty("mfa_enabled")
  private Boolean mfaEnabled;

  @JsonProperty("next_authentication")
  private NextAuthentication nextAuthentication;

}
 
 
 
