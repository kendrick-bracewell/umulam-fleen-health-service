package com.umulam.fleen.health.service;

import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.dto.authentication.ConfirmMfaDto;
import com.umulam.fleen.health.model.dto.authentication.MfaTypeDto;
import com.umulam.fleen.health.model.security.MfaDetail;
import lombok.NonNull;

public interface MemberService {

  Member getMemberByEmailAddress(String emailAddress);

  boolean isMemberExists(@NonNull String emailAddress);

  boolean isEmailAddressExists(String emailAddress);

  boolean isPhoneNumberExists(String phoneNumber);

  Member save(Member member);

  boolean enableMfa(Integer memberId, String secret);

  boolean reEnableMfa(Integer memberId);

  boolean disableMfa(Integer memberId);

  MfaDetail setupMfa(Integer memberId, MfaTypeDto mfaTypeDto);

  String getTwoFaSecret(Integer memberId);

  boolean confirmMfa(String username, ConfirmMfaDto dto);
}
