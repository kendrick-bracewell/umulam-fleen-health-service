package com.umulam.fleen.health.service;

import com.umulam.fleen.health.model.dto.authentication.ResendVerificationCodeDto;
import com.umulam.fleen.health.model.dto.authentication.SignInDto;
import com.umulam.fleen.health.model.dto.authentication.SignUpDto;
import com.umulam.fleen.health.model.dto.authentication.VerificationCodeDto;
import com.umulam.fleen.health.model.response.FleenHealthResponse;
import com.umulam.fleen.health.model.response.authentication.SignInResponse;
import com.umulam.fleen.health.model.response.authentication.SignUpResponse;
import com.umulam.fleen.health.model.security.FleenUser;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

public interface AuthenticationService {

  SignUpResponse signUp(SignUpDto dto);

  @Transactional
  SignUpResponse completeSignUp(VerificationCodeDto verificationCodeDto, FleenUser fleenUser);

  FleenHealthResponse resendVerificationCode(ResendVerificationCodeDto dto, FleenUser fleenUser);

  void signOut(String username);

  String getLoggedInUserEmailAddress();

  Authentication authenticate(String username, String password);

  SignInResponse signIn(SignInDto dto);

  String createAccessToken(FleenUser user);

  String createRefreshToken(FleenUser user);

  void saveToken(String subject, String token);

  void setContext(Authentication authentication);

  SignInResponse refreshToken(String token);
}
