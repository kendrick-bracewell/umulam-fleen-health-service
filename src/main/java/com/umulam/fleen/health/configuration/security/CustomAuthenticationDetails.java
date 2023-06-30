package com.umulam.fleen.health.configuration.security;

import lombok.Getter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

@Getter
public class CustomAuthenticationDetails extends WebAuthenticationDetails {

  private static final String VERIFICATION_CODE_KEY = "verification_code";
  private String verificationCode;

  public CustomAuthenticationDetails(HttpServletRequest request) {
    super(request);
    this.verificationCode = request.getParameter(VERIFICATION_CODE_KEY);
  }
}
