package com.umulam.fleen.health.service;

import com.umulam.fleen.health.constant.verification.ProfileVerificationStatus;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.dto.authentication.ConfirmMfaDto;
import com.umulam.fleen.health.model.dto.authentication.MfaTypeDto;
import com.umulam.fleen.health.model.dto.authentication.UpdatePasswordDto;
import com.umulam.fleen.health.model.security.MfaDetail;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface MemberService {

  Member getMemberByEmailAddress(String emailAddress);

  boolean isMemberExists(@NonNull String emailAddress);

  boolean isEmailAddressExists(String emailAddress);

  boolean isPhoneNumberExists(String phoneNumber);

  Member save(Member member);

  @Transactional
  void reEnableMfa(Integer memberId);

  @Transactional
  void disableMfa(Integer memberId);

  @Transactional
  MfaDetail setupMfa(Integer memberId, MfaTypeDto mfaTypeDto);

  String getTwoFaSecret(Integer memberId);

  @Transactional
  boolean confirmMfaSetup(String username, ConfirmMfaDto dto);

  void updatePassword(String username, UpdatePasswordDto dto);

  ProfileVerificationStatus getVerificationStatus(Integer memberId);
}
