package com.umulam.fleen.health.model.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignInResponse {

  private String token;
  private String refreshToken;

}
