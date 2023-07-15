package com.umulam.fleen.health.controller;

import com.umulam.fleen.health.model.dto.authentication.ConfirmMfaDto;
import com.umulam.fleen.health.model.dto.authentication.MfaTypeDto;
import com.umulam.fleen.health.model.response.FleenHealthResponse;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.security.MfaDetail;
import com.umulam.fleen.health.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.*;

@Slf4j
@RestController
@RequestMapping(value = "mfa")
@PreAuthorize("hasAnyRole('USER', 'PROFESSIONAL', 'BUSINESS')")
public class MfaController {

  private final MemberService memberService;
  public MfaController(MemberService memberService) {
    this.memberService = memberService;
  }

  @PutMapping(value = "/setup")
  public MfaDetail setupMfa(@AuthenticationPrincipal FleenUser user, @Valid @RequestBody MfaTypeDto dto) {
    return memberService.setupMfa(user.getId(), dto);
  }

  @PutMapping(value = "/confirm-mfa-setup")
  public FleenHealthResponse confirmMfaSetup(@AuthenticationPrincipal FleenUser user, @Valid @RequestBody ConfirmMfaDto dto) {
    memberService.confirmMfaSetup(user.getUsername(), dto);
    return new FleenHealthResponse(MFA_SET_SUCCESSFULLY);
  }

  @PutMapping(value = "/re-enable")
  public FleenHealthResponse reEnableMfa(@AuthenticationPrincipal FleenUser user) {
    memberService.reEnableMfa(user);
    return new FleenHealthResponse(MFA_RE_ENABLED);
  }

  @PutMapping(value = "/disable")
  public FleenHealthResponse disableMfa(@AuthenticationPrincipal FleenUser user) {
    memberService.disableMfa(user);
    return new FleenHealthResponse(MFA_DISABLED);
  }
}
