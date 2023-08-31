package com.umulam.fleen.health.controller;

import com.umulam.fleen.health.constant.base.FleenHealthConstant;
import com.umulam.fleen.health.model.dto.authentication.UpdatePasswordDto;
import com.umulam.fleen.health.model.dto.member.*;
import com.umulam.fleen.health.model.response.FleenHealthResponse;
import com.umulam.fleen.health.model.response.member.GetMemberUpdateDetailsResponse;
import com.umulam.fleen.health.model.response.member.SendUpdateEmailAddressOrPhoneNumberVerificationCodeResponse;
import com.umulam.fleen.health.model.response.member.UpdateEmailAddressOrPhoneNumberResponse;
import com.umulam.fleen.health.model.response.member.UpdateMemberDetailsResponse;
import com.umulam.fleen.health.model.response.other.DeleteResponse;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.service.AuthenticationService;
import com.umulam.fleen.health.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.*;

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

  @GetMapping(value = "/get-details")
  public GetMemberUpdateDetailsResponse getDetails(@AuthenticationPrincipal FleenUser user) {
    return memberService.getMemberGetUpdateDetailsResponse(user);
  }

  @GetMapping("/update-details")
  public GetMemberUpdateDetailsResponse getUpdateDetails(@AuthenticationPrincipal FleenUser user) {
    return memberService.getMemberGetUpdateDetailsResponse(user);
  }

  @PutMapping(value = "/update-details")
  public UpdateMemberDetailsResponse updateDetails(@Valid @RequestBody UpdateMemberDetailsDto dto, @AuthenticationPrincipal FleenUser user) {
    return memberService.updateMemberDetails(dto, user);
  }

  @PostMapping(value = "/send-update-email-address-phone-number-code")
  public SendUpdateEmailAddressOrPhoneNumberVerificationCodeResponse sendUpdatePhoneNumberCode(
          @Valid @RequestBody UpdateEmailAddressOrPhoneNumberDto dto,
          @AuthenticationPrincipal FleenUser user) {
    memberService.sendUpdateEmailAddressOrPhoneNumberCode(dto, user);
    return new SendUpdateEmailAddressOrPhoneNumberVerificationCodeResponse(FleenHealthConstant.VERIFICATION_CODE_SENT_MESSAGE);
  }

  @PutMapping(value = "/confirm-update-email-address")
  public UpdateEmailAddressOrPhoneNumberResponse confirmUpdatePhoneNumber(
          @Valid @RequestBody ConfirmUpdateEmailAddressDto dto,
          @AuthenticationPrincipal FleenUser user) {
    return memberService.confirmUpdateEmailAddress(dto, user);
  }

  @PutMapping(value ="/confirm-update-phone-number")
  public UpdateEmailAddressOrPhoneNumberResponse confirmUpdateEmailAddress(
          @Valid @RequestBody ConfirmUpdatePhoneNumberDto dto,
          @AuthenticationPrincipal FleenUser user) {
    return memberService.confirmUpdatePhoneNumber(dto, user);
  }

  @PutMapping(value = "/update-profile-photo")
  public FleenHealthResponse updateProfilePhoto(
          @Valid @RequestBody UpdateProfilePhotoDto dto,
          @AuthenticationPrincipal FleenUser user) {
    memberService.updateProfilePhoto(dto, user);
    return new FleenHealthResponse(PROFILE_PHOTO_UPDATED);
  }

  @PutMapping(value = "/update-password")
  public Object updatePassword(@Valid @RequestBody UpdatePasswordDto dto, @AuthenticationPrincipal FleenUser user) {
    memberService.updatePassword(user.getUsername(), dto);
    return new FleenHealthResponse(PASSWORD_CHANGED_UPDATED);
  }

  @GetMapping(value = "/sign-out")
  @PreAuthorize("hasAnyRole('USER', 'PROFESSIONAL', 'ADMINISTRATOR', 'SUPERADMINISTRATOR')")
  public FleenHealthResponse signOut(@AuthenticationPrincipal FleenUser user) {
    authenticationService.signOut(user.getUsername());
    return new FleenHealthResponse(SUCCESS_MESSAGE);
  }

  @DeleteMapping(value ="/delete-profile-photo")
  public DeleteResponse removeProfilePhoto(@AuthenticationPrincipal FleenUser user) {
    memberService.removeProfilePhoto(user);
    return new DeleteResponse();
  }
}
