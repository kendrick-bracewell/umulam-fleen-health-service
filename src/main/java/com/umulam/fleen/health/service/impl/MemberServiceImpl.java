package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.configuration.aws.s3.S3BucketNames;
import com.umulam.fleen.health.constant.CommonEmailMessageTemplateDetails;
import com.umulam.fleen.health.constant.MemberStatusType;
import com.umulam.fleen.health.constant.VerificationMessageType;
import com.umulam.fleen.health.constant.authentication.MfaSetupStatus;
import com.umulam.fleen.health.constant.authentication.MfaType;
import com.umulam.fleen.health.constant.authentication.VerificationType;
import com.umulam.fleen.health.constant.member.MemberGender;
import com.umulam.fleen.health.constant.verification.ProfileVerificationStatus;
import com.umulam.fleen.health.exception.authentication.InvalidVerificationCodeException;
import com.umulam.fleen.health.exception.authentication.MfaGenerationFailedException;
import com.umulam.fleen.health.exception.authentication.MfaVerificationFailed;
import com.umulam.fleen.health.exception.member.*;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.MemberStatus;
import com.umulam.fleen.health.model.domain.Role;
import com.umulam.fleen.health.model.dto.authentication.ConfirmMfaDto;
import com.umulam.fleen.health.model.dto.authentication.MfaTypeDto;
import com.umulam.fleen.health.model.dto.authentication.UpdatePasswordDto;
import com.umulam.fleen.health.model.dto.member.*;
import com.umulam.fleen.health.model.dto.role.UpdateMemberRoleDto;
import com.umulam.fleen.health.model.mapper.MemberMapper;
import com.umulam.fleen.health.model.mapper.RoleMapper;
import com.umulam.fleen.health.model.request.PreVerificationOrAuthenticationRequest;
import com.umulam.fleen.health.model.response.member.GetMemberUpdateDetailsResponse;
import com.umulam.fleen.health.model.response.member.UpdateEmailAddressOrPhoneNumberResponse;
import com.umulam.fleen.health.model.response.member.UpdateMemberDetailsResponse;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.security.MfaDetail;
import com.umulam.fleen.health.model.view.RoleView;
import com.umulam.fleen.health.model.view.member.MemberView;
import com.umulam.fleen.health.repository.jpa.MemberJpaRepository;
import com.umulam.fleen.health.service.*;
import com.umulam.fleen.health.service.external.aws.EmailServiceImpl;
import com.umulam.fleen.health.service.external.aws.MobileTextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.umulam.fleen.health.constant.base.GeneralConstant.UPDATE_EMAIL_CACHE_PREFIX;
import static com.umulam.fleen.health.constant.base.GeneralConstant.UPDATE_PHONE_NUMBER_CACHE_PREFIX;
import static com.umulam.fleen.health.util.DateTimeUtil.toLocalDateTime;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.capitalize;

@Slf4j
@Service
@Primary
public class MemberServiceImpl implements MemberService, CommonAuthAndVerificationService, PasswordService {

  protected final MemberJpaRepository repository;
  protected final MfaService mfaService;
  protected final AuthenticationService authenticationService;
  protected  final CacheService cacheService;
  protected final MobileTextService mobileTextService;
  protected final EmailServiceImpl emailService;
  protected final S3Service s3Service;
  protected final MemberStatusService memberStatusService;
  protected final RoleService roleService;
  protected final S3BucketNames bucketNames;
  protected final PasswordEncoder passwordEncoder;

  public MemberServiceImpl(MemberJpaRepository repository,
                           MfaService mfaService,
                           @Lazy AuthenticationService authenticationService,
                           CacheService cacheService,
                           MobileTextService mobileTextService,
                           EmailServiceImpl emailService,
                           S3Service s3Service,
                           MemberStatusService memberStatusService,
                           RoleService roleService,
                           S3BucketNames bucketNames,
                           PasswordEncoder passwordEncoder) {
    this.repository = repository;
    this.mfaService = mfaService;
    this.authenticationService = authenticationService;
    this.cacheService = cacheService;
    this.mobileTextService = mobileTextService;
    this.emailService = emailService;
    this.s3Service = s3Service;
    this.memberStatusService = memberStatusService;
    this.roleService = roleService;
    this.bucketNames = bucketNames;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Member getMemberByEmailAddress(String emailAddress) {
    return repository
            .findByEmailAddress(emailAddress)
            .orElse(null);
  }

  @Override
  @Transactional(readOnly = true)
  public MemberView findMemberById(Long id) {
    Optional<Member> memberExists = repository.findById(id);
    if (memberExists.isEmpty()) {
      throw new MemberNotFoundException(id);
    }
    return toMemberView(memberExists.get());
  }

  @Override
  public List<MemberView> toMemberViews(List<Member> members) {
    return MemberMapper.toMemberViews(members);
  }

  @Override
  public MemberView toMemberView(Member member) {
    return MemberMapper.toMemberView(member);
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
  public void reEnableMfa(FleenUser user) {
    getMember(user.getEmailAddress());
    repository.reEnableTwoFa(user.getId());
  }

  @Override
  @Transactional
  public void disableMfa(FleenUser user) {
    getMember(user.getEmailAddress());
    repository.disableTwoFa(user.getId());
  }

  @Override
  public String getTwoFaSecret(Long memberId) {
    return repository.getTwoFaSecret(memberId);
  }

  @Override
  @Transactional
  public Member save(Member member) {
    return repository.save(member);
  }

  @Override
  @Transactional
  public MfaDetail setupMfa(Long memberId, MfaTypeDto mfaTypeDto) {
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
      mfaDetail.setMfaSetupStatus(MfaSetupStatus.COMPLETE);
      mfaDetail.setMfaType(member.getMfaType().name());
      return mfaDetail;
    }

    switch (mfaType) {
      case PHONE:
        authenticationService.saveAndSendMfaVerification(member, VerificationType.PHONE, MfaType.PHONE);
        break;

      case EMAIL:
        authenticationService.saveAndSendMfaVerification(member, VerificationType.EMAIL, MfaType.EMAIL);
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
        return true;
      } else {
        throw new InvalidVerificationCodeException(dto.getCode());
      }
    }

    authenticationService.validateMfaSetupCode(username, dto.getCode(), mfaType);
    if (mfaType == MfaType.PHONE) {
      member.setPhoneNumberVerified(true);
      member.setMfaType(MfaType.PHONE);
    } else {
      member.setEmailAddressVerified(true);
      member.setMfaType(MfaType.EMAIL);
    }

    member.setMfaSecret(null);
    member.setMfaEnabled(true);
    save(member);
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
    Member member = getMember(username);
    if (passwordEncoder.matches(dto.getOldPassword(), member.getPassword())) {
      repository.updatePassword(member.getId(), createEncodedPassword(dto.getPassword()));
      return;
    }
    throw new UpdatePasswordFailedException();
  }

  @Override
  public ProfileVerificationStatus getVerificationStatus(Long memberId) {
    return repository.getProfileVerificationStatus(memberId);
  }

  @Override
  public GetMemberUpdateDetailsResponse getMemberGetUpdateDetailsResponse(FleenUser user) {
    GetMemberUpdateDetailsResponse response = repository.findMemberDetailsById(user.getId());
    if (nonNull(response)) {
      return response;
    }
    throw new UserNotFoundException(user.getEmailAddress());
  }

  @Override
  @Transactional
  public UpdateMemberDetailsResponse updateMemberDetails(UpdateMemberDetailsDto dto, FleenUser user) {
    Member member = getMember(user.getEmailAddress());
    setUpdateMemberDetails(member, dto);
    save(member);
    return new UpdateMemberDetailsResponse(dto.getFirstName(), dto.getLastName(), member.getGender(), member.getDateOfBirth());
  }

  @Override
  @Transactional
  public UpdateMemberDetailsResponse updateMemberDetails(UpdateMemberDetailsDto dto, Long memberId) {
    Member member = getMember(memberId);
    setUpdateMemberDetails(member, dto);
    save(member);
    return new UpdateMemberDetailsResponse(dto.getFirstName(), dto.getLastName(), member.getGender(), member.getDateOfBirth());
  }

  private void setUpdateMemberDetails(Member member, UpdateMemberDetailsDto dto) {
    member.setFirstName(capitalize(dto.getFirstName()));
    member.setLastName(capitalize(dto.getLastName()));
    member.setGender(MemberGender.valueOf(dto.getGender()));
    member.setDateOfBirth(toLocalDateTime(dto.getDateOfBirth()));
    member.setAddress(dto.getAddress());
  }

  protected Member getMember(String emailAddress) {
    Member member = getMemberByEmailAddress(emailAddress);
    if (Objects.isNull(member)) {
      throw new UserNotFoundException(emailAddress);
    }
    return member;
  }

  @Override
  @Transactional
  public void sendUpdateEmailAddressOrPhoneNumberCode(UpdateEmailAddressOrPhoneNumberDto dto, FleenUser user) {
    VerificationType verificationType = VerificationType.valueOf(dto.getVerificationType());
    Member member = getMember(user.getEmailAddress());

    String code = getRandomSixDigitOtp();
    FleenUser freshUser = FleenUser.fromMemberBasic(member);
    PreVerificationOrAuthenticationRequest request = createUpdateProfileRequest(code, freshUser);

    if (verificationType == VerificationType.EMAIL) {
      sendVerificationMessage(request, VerificationType.EMAIL);
      saveUpdateEmailOtp(member.getEmailAddress(), code);
    } else {
      sendVerificationMessage(request, VerificationType.PHONE);
      saveUpdatePhoneNumberOtp(member.getEmailAddress(), code);
    }
  }

  @Override
  @Transactional
  public UpdateEmailAddressOrPhoneNumberResponse confirmUpdateEmailAddress(ConfirmUpdateEmailAddressDto dto, FleenUser user) {
    String username = user.getEmailAddress();
    String verificationKey = getUpdateEmailCacheKey(username);
    String code = dto.getCode();

    validateSmsAndEmailVerificationCode(verificationKey, code);
    Member member = getMember(user.getEmailAddress());

    Optional<Member> memberEmailExists = repository.findByEmailAddress(dto.getEmailAddress());
    if (memberEmailExists.isPresent()) {
      Member memberEmail = memberEmailExists.get();
      if (!(memberEmail.getId().equals(user.getId()))) {
        throw new EmailAddressAlreadyExistsException(dto.getEmailAddress());
      }
    }

    member.setEmailAddress(dto.getEmailAddress().toLowerCase());
    member.setEmailAddressVerified(true);
    save(member);
    clearUpdateEmailAddressOtp(username);

    return new UpdateEmailAddressOrPhoneNumberResponse(dto.getEmailAddress(), null);
  }

  @Override
  @Transactional
  public UpdateEmailAddressOrPhoneNumberResponse confirmUpdatePhoneNumber(ConfirmUpdatePhoneNumberDto dto, FleenUser user) {
    String username = user.getEmailAddress();
    String verificationKey = getUpdatePhoneNumberCacheKey(username);
    String code = dto.getCode();

    validateSmsAndEmailVerificationCode(verificationKey, code);
    Member member = getMember(user.getEmailAddress());

    Optional<Member> memberPhoneExists = repository.findByPhoneNumber(dto.getPhoneNumber());
    if (memberPhoneExists.isPresent()) {
      Member memberPhone = memberPhoneExists.get();
      if (!(memberPhone.getId().equals(user.getId()))) {
        throw new PhoneNumberAlreadyExistsException(dto.getPhoneNumber());
      }
    }

    member.setPhoneNumber(dto.getPhoneNumber());
    member.setPhoneNumberVerified(true);
    save(member);
    clearUpdatePhoneNumberOtp(username);

    return new UpdateEmailAddressOrPhoneNumberResponse(null, dto.getPhoneNumber());
  }

  @Override
  @Transactional
  public void updateProfilePhoto(UpdateProfilePhotoDto dto, FleenUser user) {
    Member member = getMember(user.getEmailAddress());
    if (nonNull(member.getProfilePhoto())) {
      s3Service.deleteObject(bucketNames.getProfilePhoto(), s3Service.getObjectKeyFromUrl(member.getProfilePhoto()));
    }
    member.setProfilePhoto(dto.getProfilePhoto());
    save(member);
  }

  @Override
  @Transactional
  public void removeProfilePhoto(FleenUser user) {
    Member member = getMember(user.getEmailAddress());
    if (nonNull(member.getProfilePhoto())) {
      String key = s3Service.getObjectKeyFromUrl(member.getProfilePhoto());
      s3Service.deleteObject(bucketNames.getProfilePhoto(), key);
      member.setProfilePhoto(null);
      save(member);
    }
  }

  /**
   * <p>Prefix a user's identifier with a predefined key used to save an update email otp</p>
   * <br/>
   *
   * @param username a user identifier found on the system or is to be registered on the system
   * @return a string concatenation of a predefined prefix and the user's identifier
   */
  public static String getUpdateEmailCacheKey(String username) {
    return UPDATE_EMAIL_CACHE_PREFIX.concat(username);
  }

  /**
   * <p>Save the update email otp.</p>
   * <br/>
   *
   * @param subject the user's identifier to associate with the otp
   * @param otp a random code associated with the user's identifier during the update email process
   */
  @Override
  public void saveUpdateEmailOtp(String subject, String otp) {
    cacheService.set(getUpdateEmailCacheKey(subject), otp, Duration.ofMinutes(3));
  }

  /**
   * <p>Remove the user's update email otp after successful update of the user's email address.</p>
   * <br/>
   *
   * @param username the user's identifier associated with the update email otp or code
   */
  private void clearUpdateEmailAddressOtp(String username) {
    cacheService.delete(getUpdateEmailCacheKey(username));
  }


  /**
   * <p>Prefix a user's identifier with a predefined key used to save an update phone number otp</p>
   * <br/>
   *
   * @param username a user identifier found on the system or is to be registered on the system
   * @return a string concatenation of a predefined prefix and the user's identifier
   */
  public static String getUpdatePhoneNumberCacheKey(String username) {
    return UPDATE_PHONE_NUMBER_CACHE_PREFIX.concat(username);
  }

  /**
   * <p>Save the update phone number otp.</p>
   * <br/>
   *
   * @param subject the user's identifier to associate with the otp
   * @param otp a random code associated with the user's identifier during the update phone number process
   */
  @Override
  public void saveUpdatePhoneNumberOtp(String subject, String otp) {
    cacheService.set(getUpdatePhoneNumberCacheKey(subject), otp, Duration.ofMinutes(3));
  }

  /**
   * <p>Remove the user's update phone number otp after successful update of the user's phone number.</p>
   * <br/>
   *
   * @param username the user's identifier associated with the update phone number otp or code
   */
  private void clearUpdatePhoneNumberOtp(String username) {
    cacheService.delete(getUpdatePhoneNumberCacheKey(username));
  }

  @Override
  public void updateMemberStatus(UpdateMemberStatusDto dto, Long memberId) {
    getMember(memberId);
    MemberStatusType memberStatusType = MemberStatusType.valueOf(dto.getMemberStatus());
    MemberStatus memberStatus = memberStatusService.getMemberStatusByCode(memberStatusType.name());
    repository.updateMemberStatus(memberId, memberStatus);
  }

  @Override
  @Transactional
  public void updateMemberRole(UpdateMemberRoleDto dto, Long memberId) {
    Member member = getMember(memberId);
    List<Role> roles = roleService.getRolesById(dto.getIds());
    member.getRoles().addAll(roles);
    save(member);
  }

  @Override
  @Transactional(readOnly = true)
  public List<RoleView> getMemberRoles(Long memberId) {
    getMember(memberId);
    List<Role> roles = new ArrayList<>(repository.getMemberRole(memberId));
    return RoleMapper.toRoleViews(roles);
  }

  private PreVerificationOrAuthenticationRequest createUpdateProfileRequest(String code, FleenUser user) {
    CommonEmailMessageTemplateDetails templateDetails = CommonEmailMessageTemplateDetails.PROFILE_UPDATE;
    PreVerificationOrAuthenticationRequest request = createVerificationRequest(code, user);
    request.setEmailMessageTitle(templateDetails.getEmailMessageSubject());
    request.setSmsMessage(getVerificationSmsMessage(VerificationMessageType.PROFILE_UPDATE));

    String emailBody = getVerificationEmailBody(templateDetails.getTemplateName(), request);
    request.setEmailMessageBody(emailBody);
    return request;
  }

  protected Member getMember(Long memberId) {
    Optional<Member> memberExists = repository.findById(memberId);

    if (memberExists.isEmpty()) {
      throw new MemberNotFoundException(memberId);
    }
    return memberExists.get();
  }

  @Override
  public Member getMemberById(Long memberId) {
    Optional<Member> memberExists = repository.findById(memberId);

    if (memberExists.isEmpty()) {
      throw new UserNotFoundException(memberId);
    }
    return memberExists.get();
  }

  @Override
  public MobileTextService getMobileTextService() {
    return mobileTextService;
  }

  @Override
  public EmailServiceImpl getEmailService() {
    return emailService;
  }

  @Override
  public CacheService getCacheService() {
    return cacheService;
  }

  @Override
  public ProfileVerificationMessageService getProfileVerificationMessageService() {
    return null;
  }

  @Override
  public VerificationHistoryService getVerificationHistoryService() {
    return null;
  }

  @Override
  public PasswordEncoder getPasswordEncoder() {
    return passwordEncoder;
  }

  @Override
  public MemberStatusService getMemberStatusService() {
    return memberStatusService;
  }
}
