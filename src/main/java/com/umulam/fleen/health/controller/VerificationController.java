package com.umulam.fleen.health.controller;

import com.umulam.fleen.health.model.dto.authentication.ConfirmMfaDto;
import com.umulam.fleen.health.model.dto.authentication.ResendVerificationCodeDto;
import com.umulam.fleen.health.model.dto.authentication.VerificationCodeDto;
import com.umulam.fleen.health.model.response.FleenHealthResponse;
import com.umulam.fleen.health.model.response.authentication.SignInResponse;
import com.umulam.fleen.health.model.response.authentication.SignUpResponse;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.umulam.fleen.health.constant.authentication.AuthenticationConstant.AUTH_HEADER_PREFIX;
import static com.umulam.fleen.health.constant.authentication.AuthenticationConstant.REFRESH_TOKEN_HEADER_KEY;

@Slf4j
@RestController
@RequestMapping(value = "verification")
public class VerificationController {

  private final AuthenticationService authenticationService;

  public VerificationController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @PostMapping(value = "/confirm-sign-up")
  @PreAuthorize("hasAnyRole('PRE_VERIFIED_USER', 'PRE_VERIFIED_PROFESSIONAL', 'PRE_VERIFIED_BUSINESS')")
  public SignUpResponse completeSignUp(@AuthenticationPrincipal FleenUser user,
                                       @Valid @RequestBody VerificationCodeDto dto) {
    return authenticationService.completeSignUp(dto, user);
  }

  @PostMapping(value = "/resend-verification-code")
  @PreAuthorize("hasAnyRole('PRE_VERIFIED_USER', 'PRE_VERIFIED_PROFESSIONAL', 'PRE_VERIFIED_BUSINESS')")
  public FleenHealthResponse resendVerificationCode(@AuthenticationPrincipal FleenUser user,
                                                    @Valid @RequestBody ResendVerificationCodeDto dto) {
    return authenticationService.resendVerificationCode(dto, user);
  }

  @GetMapping(value = "/refresh-token")
  @PreAuthorize("hasRole('REFRESH_TOKEN')")
  public SignInResponse refreshToken(@AuthenticationPrincipal FleenUser user, @RequestHeader(value = REFRESH_TOKEN_HEADER_KEY) String token) {
    token = token.replace(AUTH_HEADER_PREFIX, "");
    return authenticationService.refreshToken(user.getUsername(), token);
  }

  @PostMapping(value = "/validate-mfa")
  @PreAuthorize("hasRole('PRE_AUTHENTICATED_USER')")
  public SignInResponse validateMfa(@AuthenticationPrincipal FleenUser user, @Valid @RequestBody ConfirmMfaDto dto) {
    return authenticationService.validateMfa(user, dto);
  }
}
