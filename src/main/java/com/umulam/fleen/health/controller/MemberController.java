package com.umulam.fleen.health.controller;

import com.umulam.fleen.health.model.response.FleenHealthResponse;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.service.AuthenticationService;
import com.umulam.fleen.health.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "member")
public class MemberController {

  private final MemberService memberService;

  private final AuthenticationService authenticationService;

  public MemberController(MemberService memberService,
                          AuthenticationService authenticationService) {
    this.memberService = memberService;
    this.authenticationService = authenticationService;
  }

  @PutMapping(value = "/update-details")
  public Object updateDetails() {
    return null;
  }

  @PutMapping(value = "/update-phone-number")
  public Object updatePhoneNumber() {
    return null;
  }

  @PutMapping(value = "/confirm-update-phone-number")
  public Object confirmUpdatePhoneNumber() {
    return null;
  }

  @PutMapping(value = "/update-email-address")
  public Object updateEmailAddress() {
    return null;
  }

  @PutMapping(value = "/update-profile-photo")
  public Object updateProfilePhoto() {
    return null;
  }

  @GetMapping(value = "/sign-out")
  @PreAuthorize("hasAnyRole('USER', 'PROFESSIONAL', 'ADMINISTRATOR', 'SUPERADMINISTRATOR')")
  public FleenHealthResponse signOut(@AuthenticationPrincipal FleenUser user) {
    authenticationService.signOut(user.getUsername());
    return new FleenHealthResponse("Success");
  }
}
