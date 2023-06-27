package com.umulam.fleen.health.service;

import com.umulam.fleen.health.model.dto.authentication.SignInDto;
import com.umulam.fleen.health.model.dto.authentication.SignUpDto;
import com.umulam.fleen.health.model.response.SignInResponse;
import com.umulam.fleen.health.model.security.UserDetailsImpl;
import org.springframework.security.core.Authentication;

public interface AuthenticationService {

  SignInResponse signUp(SignUpDto dto);

  void logout(String username);

  String getLoggedInUserEmailAddress();

  Authentication authenticate(String username, String password);

  SignInResponse login(SignInDto dto);

  String createAccessToken(UserDetailsImpl user);

  String createRefreshToken(UserDetailsImpl user);

  void saveToken(String subject, String token);

  void setContext(Authentication authentication);

  SignInResponse refreshToken(String token);
}
