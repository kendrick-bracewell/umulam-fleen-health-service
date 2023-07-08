package com.umulam.fleen.health.service;

import com.umulam.fleen.health.adapter.google.recaptcha.model.response.ReCaptchaResponse;
import com.umulam.fleen.health.model.dto.authentication.*;
import com.umulam.fleen.health.model.response.FleenHealthResponse;
import com.umulam.fleen.health.model.response.authentication.SignInResponse;
import com.umulam.fleen.health.model.response.authentication.SignUpResponse;
import com.umulam.fleen.health.model.security.FleenUser;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

public interface AuthenticationService {

  SignUpResponse signUp(SignUpDto dto);

  @Transactional
  SignUpResponse completeSignUp(VerificationCodeDto dto, FleenUser user);

  FleenHealthResponse resendPreVerificationCode(ResendVerificationCodeDto dto, FleenUser user);

  void signOut(String username);

  SignInResponse validateSignInMfa(FleenUser user, ConfirmMfaDto dto);

  Authentication authenticate(String username, String password);

  SignInResponse signIn(SignInDto dto);

  abstract String createAccessToken(FleenUser user);

  abstract String createRefreshToken(FleenUser user);

  void saveToken(String subject, String token);

  void setContext(Authentication authentication);

  SignInResponse refreshToken(String username, String token);

  void saveRefreshToken(String subject, String token);

  void forgotPassword(ForgotPasswordDto dto);

  InitiatePasswordChangeResponse validateResetPasswordCode(ResetPasswordDto dto);

  void changePassword(String username, ChangePasswordDto dto);

  ReCaptchaResponse verifyReCaptcha(String reCaptchaToken);
}
