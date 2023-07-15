package com.umulam.fleen.health.service;

import com.umulam.fleen.health.constant.verification.ProfileVerificationStatus;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.dto.authentication.ConfirmMfaDto;
import com.umulam.fleen.health.model.dto.authentication.MfaTypeDto;
import com.umulam.fleen.health.model.dto.authentication.UpdatePasswordDto;
import com.umulam.fleen.health.model.dto.member.*;
import com.umulam.fleen.health.model.dto.role.UpdateMemberRoleDto;
import com.umulam.fleen.health.model.response.member.GetMemberUpdateDetailsResponse;
import com.umulam.fleen.health.model.response.member.UpdateEmailAddressOrPhoneNumberResponse;
import com.umulam.fleen.health.model.response.member.UpdateMemberDetailsResponse;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.security.MfaDetail;
import com.umulam.fleen.health.model.view.MemberView;
import com.umulam.fleen.health.model.view.RoleView;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemberService {

  Member getMemberByEmailAddress(String emailAddress);

  @Transactional(readOnly = true)
  MemberView findMemberById(Integer id);

  List<MemberView> toMemberViews(List<Member> members);

  MemberView toMemberView(Member member);

  boolean isMemberExists(@NonNull String emailAddress);

  boolean isEmailAddressExists(String emailAddress);

  boolean isPhoneNumberExists(String phoneNumber);

  Member save(Member member);

  @Transactional
  void reEnableMfa(FleenUser user);

  @Transactional
  void disableMfa(FleenUser user);

  @Transactional
  MfaDetail setupMfa(Integer memberId, MfaTypeDto mfaTypeDto);

  String getTwoFaSecret(Integer memberId);

  @Transactional
  boolean confirmMfaSetup(String username, ConfirmMfaDto dto);

  void updatePassword(String username, UpdatePasswordDto dto);

  ProfileVerificationStatus getVerificationStatus(Integer memberId);

  GetMemberUpdateDetailsResponse getMemberGetUpdateDetailsResponse(FleenUser user);

  UpdateMemberDetailsResponse updateMemberDetails(UpdateMemberDetailsDto dto, FleenUser user);

  @Transactional
  UpdateMemberDetailsResponse updateMemberDetails(UpdateMemberDetailsDto dto, Integer memberId);

  void sendUpdateEmailAddressOrPhoneNumberCode(UpdateEmailAddressOrPhoneNumberDto dto, FleenUser user);

  @Transactional
  UpdateEmailAddressOrPhoneNumberResponse confirmUpdateEmailAddress(ConfirmUpdateEmailAddressDto dto, FleenUser user);

  @Transactional
  UpdateEmailAddressOrPhoneNumberResponse confirmUpdatePhoneNumber(ConfirmUpdatePhoneNumberDto dto, FleenUser user);

  @Transactional
  void updateProfilePhoto(UpdateProfilePhotoDto dto, FleenUser user);

  @Transactional
  void removeProfilePhoto(FleenUser user);

  void saveUpdateEmailOtp(String subject, String otp);

  void saveUpdatePhoneNumberOtp(String subject, String otp);

  @Transactional
  void updateMemberStatus(UpdateMemberStatusDto dto, Integer memberId);

  @Transactional
  void updateMemberRole(UpdateMemberRoleDto dto, Integer memberId);

  @Transactional(readOnly = true)
  List<RoleView> getMemberRoles(Integer memberId);
}
