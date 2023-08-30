package com.umulam.fleen.health.service;

import com.umulam.fleen.health.adapter.google.recaptcha.model.response.ReCaptchaResponse;
import com.umulam.fleen.health.constant.authentication.AuthenticationStatus;
import com.umulam.fleen.health.constant.authentication.MfaType;
import com.umulam.fleen.health.constant.authentication.VerificationType;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.dto.authentication.*;
import com.umulam.fleen.health.model.response.FleenHealthResponse;
import com.umulam.fleen.health.model.response.authentication.ForgotPasswordResponse;
import com.umulam.fleen.health.model.response.authentication.InitiatePasswordChangeResponse;
import com.umulam.fleen.health.model.response.authentication.SignInResponse;
import com.umulam.fleen.health.model.response.authentication.SignUpResponse;
import com.umulam.fleen.health.model.security.FleenUser;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

public interface AuthenticationService {

  @Transactional
  SignUpResponse signUp(SignUpDto dto);

  @Transactional
  SignUpResponse completeSignUp(VerificationCodeDto dto, FleenUser user);

  FleenHealthResponse resendPreVerificationCode(ResendVerificationCodeDto dto, FleenUser user);

  FleenHealthResponse resendPreAuthenticationCode(ResendVerificationCodeDto dto, FleenUser user);

  @Transactional(readOnly = true)
  void signOut(String username);

  SignInResponse validateSignInMfa(FleenUser user, ConfirmMfaDto dto);

  Authentication authenticate(String username, String password);

  @Transactional
  SignInResponse signIn(SignInDto dto);

  String createAccessToken(FleenUser user);

  String createAccessToken(FleenUser user, AuthenticationStatus authenticationStatus);

  String createRefreshToken(FleenUser user);

  void saveToken(String subject, String token);

  void setContext(Authentication authentication);

  SignInResponse refreshToken(String username, String token);

  void saveRefreshToken(String subject, String token);

  @Transactional
  ForgotPasswordResponse forgotPassword(ForgotPasswordDto dto);

  @Transactional(readOnly = true)
  InitiatePasswordChangeResponse validateResetPasswordCode(ResetPasswordDto dto);

  @Transactional
  void changePassword(String username, ChangePasswordDto dto);

  @Transactional
  SignInResponse completeOnboarding(String username, ChangePasswordDto dto);

  ReCaptchaResponse verifyReCaptcha(String reCaptchaToken);

  @Transactional
  void saveAndSendMfaVerification(Member member, VerificationType verificationType, MfaType mfaType);

  void validateMfaSetupCode(String username, String code, MfaType mfaType);

  String createPassword(String rawPassword);
}
