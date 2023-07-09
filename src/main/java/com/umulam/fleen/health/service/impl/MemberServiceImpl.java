package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.constant.CommonEmailMessageTemplateDetails;
import com.umulam.fleen.health.constant.VerificationMessageType;
import com.umulam.fleen.health.constant.authentication.MfaSetupStatus;
import com.umulam.fleen.health.constant.authentication.MfaType;
import com.umulam.fleen.health.constant.authentication.VerificationType;
import com.umulam.fleen.health.constant.verification.ProfileVerificationStatus;
import com.umulam.fleen.health.exception.authentication.*;
import com.umulam.fleen.health.exception.member.UpdatePasswordFailedException;
import com.umulam.fleen.health.exception.member.UserNotFoundException;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.dto.authentication.ConfirmMfaDto;
import com.umulam.fleen.health.model.dto.authentication.MfaTypeDto;
import com.umulam.fleen.health.model.dto.authentication.UpdatePasswordDto;
import com.umulam.fleen.health.model.request.PreVerificationOrAuthenticationRequest;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.security.MfaDetail;
import com.umulam.fleen.health.repository.jpa.MemberJpaRepository;
import com.umulam.fleen.health.service.AuthenticationService;
import com.umulam.fleen.health.service.MemberService;
import com.umulam.fleen.health.service.MfaService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.AUTH;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

  private final MemberJpaRepository repository;
  private final MfaService mfaService;
  private final AuthenticationService authenticationService;
  private final PasswordEncoder passwordEncoder;

  public MemberServiceImpl(MemberJpaRepository repository,
                           MfaService mfaService,
                           AuthenticationService authenticationService,
                           PasswordEncoder passwordEncoder) {
    this.repository = repository;
    this.mfaService = mfaService;
    this.authenticationService = authenticationService;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Member getMemberByEmailAddress(String emailAddress) {
    return repository
            .findByEmailAddress(emailAddress)
            .orElse(null);
  }

  @Override
  public boolean isMemberExists(@NonNull String emailAddress) {
    return repository.findByEmailAddress(emailAddress).isPresent();
  }

  @Override
  public boolean isEmailAddressExists(String emailAddress) {
    return repository.existsByEmailAddress(emailAddress);
  }

  @Override
  public boolean isPhoneNumberExists(String phoneNumber) {
    return repository.existsByPhoneNumber(phoneNumber);
  }

  @Override
  @Transactional
  public void reEnableMfa(Integer memberId) {
    repository.reEnableTwoFa(memberId);
  }

  @Override
  @Transactional
  public void disableMfa(Integer memberId) {
    repository.disableTwoFa(memberId);
  }

  @Override
  public String getTwoFaSecret(Integer memberId) {
    return repository.getTwoFaSecret(memberId);
  }

  @Override
  @Transactional
  public Member save(Member member) {
    return repository.save(member);
  }

  @Override
  @Transactional
  public MfaDetail setupMfa(Integer memberId, MfaTypeDto mfaTypeDto) {
    MfaType mfaType = MfaType.valueOf(mfaTypeDto.getMfaType());
    Member member = repository
            .findById(memberId)
            .orElseThrow(() -> new UserNotFoundException(String.valueOf(memberId)));
    MfaDetail mfaDetail = MfaDetail.builder()
            .emailAddress(member.getEmailAddress())
            .phoneNumber(member.getPhoneNumber())
            .mfaSetupStatus(MfaSetupStatus.IN_PROGRESS)
            .mfaType(mfaType.name())
            .enabled(false)
            .build();

    if (member.getMfaType() == mfaType && mfaType != MfaType.AUTHENTICATOR) {
      mfaDetail.setEnabled(true);
      mfaDetail.setMfaType(mfaType.name());
      return mfaDetail;
    }

    switch (mfaType) {
      case SMS:
        authenticationService.sendMfaVerification(member, VerificationType.SMS);
        break;

      case EMAIL:
        authenticationService.sendMfaVerification(member, VerificationType.EMAIL);
        break;

      case AUTHENTICATOR:
        mfaDetail = generateAuthenticatorSecret();
        member.setMfaType(mfaType);
        member.setMfaEnabled(mfaDetail.isEnabled());
        member.setMfaSecret(mfaDetail.getSecret());
        break;
    }
    save(member);
    return mfaDetail;
  }

  @Override
  @Transactional
  public boolean confirmMfaSetup(String username, ConfirmMfaDto dto) {
    MfaType mfaType = MfaType.valueOf(dto.getMfaType());
    Member member = getMemberByEmailAddress(username);
    if (Objects.isNull(member)) {
      throw new MfaVerificationFailed();
    }

    if (mfaType == MfaType.AUTHENTICATOR) {
      if (mfaService.verifyAuthenticatorOtp(dto.getCode(), member.getMfaSecret())) {
        member.setMfaEnabled(true);
        member.setMfaType(MfaType.AUTHENTICATOR);
        save(member);
      } else {
        throw new InvalidVerificationCodeException(dto.getCode());
      }
    } else if (mfaType == MfaType.SMS) {
      member.setMfaSecret(null);
      member.setMfaEnabled(true);
      member.setPhoneNumberVerified(true);
      member.setMfaType(MfaType.SMS);
    } else {
      member.setMfaSecret(null);
      member.setMfaEnabled(true);
      member.setEmailAddressVerified(true);
      member.setMfaType(MfaType.EMAIL);
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
            .mfaSetupStatus(MfaSetupStatus.IN_PROGRESS)
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

    repository.updatePassword(member.getId(), passwordEncoder.encode(dto.getPassword()));
  }

  @Override
  public ProfileVerificationStatus getVerificationStatus(Integer memberId) {
    return repository.getProfileVerificationStatus(memberId);
  }
}
