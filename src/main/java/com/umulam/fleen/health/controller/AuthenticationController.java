package com.umulam.fleen.health.controller;

import com.umulam.fleen.health.model.dto.authentication.ForgotPasswordDto;
import com.umulam.fleen.health.model.dto.authentication.ResetPasswordDto;
import com.umulam.fleen.health.model.dto.authentication.SignInDto;
import com.umulam.fleen.health.model.dto.authentication.SignUpDto;
import com.umulam.fleen.health.model.response.authentication.ForgotPasswordResponse;
import com.umulam.fleen.health.model.response.authentication.InitiatePasswordChangeResponse;
import com.umulam.fleen.health.model.response.authentication.SignInResponse;
import com.umulam.fleen.health.model.response.authentication.SignUpResponse;
import com.umulam.fleen.health.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "auth")
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  public AuthenticationController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @PostMapping(value = "/sign-in")
  public SignInResponse signIn(@Valid @RequestBody SignInDto dto) {
    return authenticationService.signIn(dto);
  }

  @PostMapping(value = "/sign-up")
  public SignUpResponse signUp(@Valid @RequestBody SignUpDto dto) {
    return authenticationService.signUp(dto);
  }

  @PostMapping(value = "/forgot-password")
  public ForgotPasswordResponse forgotPassword(@Valid @RequestBody ForgotPasswordDto dto) {
    return authenticationService.forgotPassword(dto);
  }

  @PostMapping(value = "/verify-reset-password-code")
  public InitiatePasswordChangeResponse verifyResetPasswordCode(@Valid @RequestBody ResetPasswordDto dto) {
    return authenticationService.validateResetPasswordCode(dto);
  }

}
