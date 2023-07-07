package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.constant.authentication.MfaType;
import com.umulam.fleen.health.exception.authentication.InvalidAuthenticationException;
import com.umulam.fleen.health.exception.authentication.InvalidVerificationCodeException;
import com.umulam.fleen.health.exception.authentication.MfaGenerationFailedException;
import com.umulam.fleen.health.exception.authentication.VerificationFailedException;
import com.umulam.fleen.health.exception.member.UpdatePasswordFailedException;
import com.umulam.fleen.health.exception.member.UserNotFoundException;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.dto.authentication.ConfirmMfaDto;
import com.umulam.fleen.health.model.dto.authentication.MfaTypeDto;
import com.umulam.fleen.health.model.dto.authentication.UpdatePasswordDto;
import com.umulam.fleen.health.model.security.MfaDetail;
import com.umulam.fleen.health.repository.jpa.MemberJpaRepository;
import com.umulam.fleen.health.service.MemberService;
import com.umulam.fleen.health.service.MfaService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

  private final MemberJpaRepository memberJpaRepository;
  private final MfaService mfaService;
  private final PasswordEncoder passwordEncoder;

  public MemberServiceImpl(MemberJpaRepository memberJpaRepository,
                           MfaService mfaService,
                           PasswordEncoder passwordEncoder) {
    this.memberJpaRepository = memberJpaRepository;
    this.mfaService = mfaService;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Member getMemberByEmailAddress(String emailAddress) {
    return memberJpaRepository
            .findByEmailAddress(emailAddress)
            .orElse(null);
  }

  @Override
  public boolean isMemberExists(@NonNull String emailAddress) {
    return memberJpaRepository.findByEmailAddress(emailAddress).isPresent();
  }

  @Override
  public boolean isEmailAddressExists(String emailAddress) {
    return memberJpaRepository.existsByEmailAddress(emailAddress);
  }

  @Override
  public boolean isPhoneNumberExists(String phoneNumber) {
    return memberJpaRepository.existsByPhoneNumber(phoneNumber);
  }

  @Override
  public boolean enableMfa(Integer memberId, String secret) {
    memberJpaRepository.enableTwoFa(memberId, secret);
    return true;
  }

  @Override
  @Transactional
  public void reEnableMfa(Integer memberId) {
    memberJpaRepository.reEnableTwoFa(memberId);
  }

  @Override
  @Transactional
  public void disableMfa(Integer memberId) {
    memberJpaRepository.disableTwoFa(memberId);
  }

  @Override
  public String getTwoFaSecret(Integer memberId) {
    return memberJpaRepository.getTwoFaSecret(memberId);
  }

  @Override
  public Member save(Member member) {
    return memberJpaRepository.save(member);
  }

  @Override
  @Transactional
  public MfaDetail setupMfa(Integer memberId, MfaTypeDto mfaTypeDto) {
    MfaType mfaType = MfaType.valueOf(mfaTypeDto.getMfaType());
    Member member = memberJpaRepository
            .findById(memberId)
            .orElseThrow(() -> new InvalidAuthenticationException(String.valueOf(memberId)));
    MfaDetail mfaDetail = new MfaDetail();

    if (member.getMfaType() == mfaType && mfaType != MfaType.AUTHENTICATOR) {
      mfaDetail.setEnabled(true);
      mfaDetail.setMfaType(mfaType.name());
      return mfaDetail;
    }

    switch (mfaType) {
      case SMS:

      case EMAIL:
        member.setMfaType(mfaType);
        member.setMfaSecret(null);
        member.setMfaEnabled(true);
        mfaDetail.setEnabled(true);
        mfaDetail.setMfaType(mfaType.name());
        break;

      case AUTHENTICATOR:
        mfaDetail = generateAuthenticatorSecret();
        member.setMfaType(mfaType);
        member.setMfaEnabled(true);
        member.setMfaSecret(mfaDetail.getSecret());
    }
    save(member);
    return mfaDetail;
  }

  @Override
  @Transactional
  public boolean confirmMfa(String username, ConfirmMfaDto dto) {
    MfaType mfaType = MfaType.valueOf(dto.getMfaType());
    Member member = getMemberByEmailAddress(username);
    if (Objects.isNull(member)) {
      throw new VerificationFailedException();
    }

    if (mfaType == MfaType.AUTHENTICATOR) {
      if (mfaService.verifyAuthenticatorOtp(dto.getCode(), member.getMfaSecret())) {
        member.setMfaEnabled(true);
        save(member);
      } else {
        throw new InvalidVerificationCodeException(dto.getCode());
      }
    }
    return true;
  }

  private MfaDetail generateAuthenticatorSecret() {
    String secret = mfaService.generateSecretKey();
    String qrCode = mfaService.getQrCode(secret);
    if (qrCode == null) {
      throw new MfaGenerationFailedException();
    }
    return MfaDetail.builder()
            .qrCode(qrCode)
            .secret(secret)
            .enabled(false)
            .mfaType(MfaType.AUTHENTICATOR.name())
            .build();
  }

  @Override
  public void updatePassword(String username, UpdatePasswordDto dto) {
    Member member = getMemberByEmailAddress(username);
    if (Objects.isNull(member)) {
      throw new UserNotFoundException(username);
    }

    if (passwordEncoder.matches(dto.getOldPassword(), member.getPassword())) {
      throw new UpdatePasswordFailedException();
    }

    memberJpaRepository.updatePassword(member.getId(), passwordEncoder.encode(dto.getPassword()));
  }
}
