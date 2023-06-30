package com.umulam.fleen.health.controller;

import com.umulam.fleen.health.model.dto.authentication.ResendVerificationCodeDto;
import com.umulam.fleen.health.model.dto.authentication.VerificationCodeDto;
import com.umulam.fleen.health.model.response.FleenHealthResponse;
import com.umulam.fleen.health.model.response.authentication.SignUpResponse;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "verification")
public class VerificationController {

  private final AuthenticationService authenticationService;

  public VerificationController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @PostMapping(value = "/confirm-sign-up")
  @PreAuthorize("hasRole('PRE_VERIFIED_USER')")
  public SignUpResponse confirmVerification(@AuthenticationPrincipal FleenUser user,
                                            @Valid @RequestBody VerificationCodeDto dto) {
    return authenticationService.completeSignUp(dto, user);
  }

  @PostMapping(value = "/resend-verification-code")
  @PreAuthorize("hasRole('PRE_VERIFIED_USER')")
  public FleenHealthResponse resendVerificationCode(@AuthenticationPrincipal FleenUser user,
                                                    @Valid @RequestBody ResendVerificationCodeDto dto) {
    return authenticationService.resendVerificationCode(dto, user);
  }
}
