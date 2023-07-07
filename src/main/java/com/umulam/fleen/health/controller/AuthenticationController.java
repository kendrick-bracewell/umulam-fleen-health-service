package com.umulam.fleen.health.controller;

import com.umulam.fleen.health.model.dto.authentication.ForgotPasswordDto;
import com.umulam.fleen.health.model.dto.authentication.SignInDto;
import com.umulam.fleen.health.model.dto.authentication.SignUpDto;
import com.umulam.fleen.health.model.response.FleenHealthResponse;
import com.umulam.fleen.health.model.response.authentication.SignInResponse;
import com.umulam.fleen.health.model.response.authentication.SignUpResponse;
import com.umulam.fleen.health.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
  public FleenHealthResponse forgotPassword(@Valid @RequestBody ForgotPasswordDto dto) {
    authenticationService.forgotPassword(dto);
    return new FleenHealthResponse("Success");
  }

  @PostMapping(value = "/verify-reset-password-code")
  public Object verifyResetPasswordCode() {
    return null;
  }

}
