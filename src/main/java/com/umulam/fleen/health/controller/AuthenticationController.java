package com.umulam.fleen.health.controller;

import com.umulam.fleen.health.model.dto.authentication.SignInDto;
import com.umulam.fleen.health.model.dto.authentication.SignUpDto;
import com.umulam.fleen.health.model.response.authentication.SignInResponse;
import com.umulam.fleen.health.model.response.authentication.SignUpResponse;
import com.umulam.fleen.health.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.umulam.fleen.health.constant.authentication.AuthenticationConstant.REFRESH_TOKEN_HEADER_KEY;

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

  @GetMapping(value = "/refresh-token")
  public SignInResponse refreshToken(@RequestHeader(value = REFRESH_TOKEN_HEADER_KEY) String refreshToken) {
    return authenticationService.refreshToken(refreshToken);
  }
}
