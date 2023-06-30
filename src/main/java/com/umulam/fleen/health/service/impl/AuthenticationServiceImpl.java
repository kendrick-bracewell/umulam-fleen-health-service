package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.constant.EmailMessageSource;
import com.umulam.fleen.health.constant.MemberStatusType;
import com.umulam.fleen.health.constant.MessageType;
import com.umulam.fleen.health.constant.authentication.MfaType;
import com.umulam.fleen.health.constant.authentication.RoleType;
import com.umulam.fleen.health.constant.authentication.VerificationType;
import com.umulam.fleen.health.exception.authentication.ExpiredVerificationCodeException;
import com.umulam.fleen.health.exception.authentication.InvalidAuthenticationException;
import com.umulam.fleen.health.exception.authentication.InvalidVerificationCodeException;
import com.umulam.fleen.health.exception.authentication.VerificationFailedException;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.MemberStatus;
import com.umulam.fleen.health.model.domain.Role;
import com.umulam.fleen.health.model.dto.authentication.*;
import com.umulam.fleen.health.model.dto.mail.EmailDetails;
import com.umulam.fleen.health.model.json.SmsMessage;
import com.umulam.fleen.health.model.response.FleenHealthResponse;
import com.umulam.fleen.health.model.response.authentication.SignInResponse;
import com.umulam.fleen.health.model.response.authentication.SignUpResponse;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.service.*;
import com.umulam.fleen.health.service.external.aws.EmailServiceImpl;
import com.umulam.fleen.health.service.external.aws.MobileTextService;
import com.umulam.fleen.health.util.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.*;

import static com.umulam.fleen.health.constant.FleenHealthConstant.*;
import static com.umulam.fleen.health.constant.authentication.AuthenticationConstant.AUTH_CACHE_PREFIX;
import static com.umulam.fleen.health.constant.authentication.AuthenticationConstant.PRE_VERIFICATION_PREFIX;
import static com.umulam.fleen.health.constant.authentication.AuthenticationStatus.COMPLETED;
import static com.umulam.fleen.health.constant.authentication.AuthenticationStatus.IN_PROGRESS;
import static com.umulam.fleen.health.constant.authentication.MfaType.EMAIL;
import static com.umulam.fleen.health.constant.authentication.MfaType.SMS;
import static com.umulam.fleen.health.constant.authentication.RoleType.PRE_VERIFIED_USER;
import static com.umulam.fleen.health.constant.authentication.TokenType.ACCESS_TOKEN;
import static com.umulam.fleen.health.constant.authentication.TokenType.REFRESH_TOKEN;
import static com.umulam.fleen.health.util.DateTimeUtil.toHours;
import static com.umulam.fleen.health.util.FleenAuthorities.getPreAuthenticatedAuthorities;
import static com.umulam.fleen.health.util.FleenAuthorities.getPreVerifiedAuthorities;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

  private final AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService;
  private final MemberService memberService;
  private final MemberStatusService memberStatusService;
  private final RoleService roleService;
  private final MfaService mfaService;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;
  private final CacheService cacheService;
  private final MobileTextService mobileTextService;
  private final EmailServiceImpl emailService;

  public AuthenticationServiceImpl(AuthenticationManager authenticationManager,
                                   UserDetailsService userDetailsService,
                                   MemberService memberService,
                                   MemberStatusService memberStatusService,
                                   RoleService roleService,
                                   MfaService mfaService,
                                   PasswordEncoder passwordEncoder,
                                   JwtProvider jwtProvider,
                                   CacheService cacheService,
                                   MobileTextService mobileTextService,
                                   EmailServiceImpl emailService) {
    this.authenticationManager = authenticationManager;
    this.userDetailsService = userDetailsService;
    this.memberService = memberService;
    this.memberStatusService = memberStatusService;
    this.roleService = roleService;
    this.mfaService = mfaService;
    this.passwordEncoder = passwordEncoder;
    this.jwtProvider = jwtProvider;
    this.cacheService = cacheService;
    this.mobileTextService = mobileTextService;
    this.emailService = emailService;
  }

  @Override
  public SignInResponse signIn(SignInDto dto) {
    Authentication authentication = authenticate(dto.getEmailAddress(), dto.getPassword());
    if (!authentication.isAuthenticated()) {
      throw new InvalidAuthenticationException(dto.getEmailAddress());
    }

    FleenUser user = (FleenUser) authentication.getPrincipal();
    Role role = new ArrayList<>(user.getRoles()).get(0);
    String accessToken;
    String refreshToken;

    SignInResponse signInResponse = SignInResponse.builder()
            .emailAddress(user.getEmailAddress())
            .authenticationStatus(IN_PROGRESS)
            .build();

    if (MemberStatusType.INACTIVE.name().equals(user.getStatus())
        && PRE_VERIFIED_USER.name().equals(role.getCode())) {
      String otp = mfaService.generateVerificationOtp(6);
      PreVerificationDto preVerification = PreVerificationDto.builder()
              .code(otp)
              .phoneNumber(user.getPhoneNumber())
              .emailAddress(dto.getEmailAddress())
              .build();

      VerificationType verificationType = VerificationType.valueOf("EMAIL");
      initPreVerification(preVerification, verificationType);
      cacheService.set(getPreVerificationCacheKey(user.getUsername()), otp, Duration.ofMinutes(1));

      user.setAuthorities(getPreVerifiedAuthorities());
      Authentication authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
      accessToken = createAccessToken(user);
      refreshToken = createRefreshToken(user);
      setContext(authenticationToken);

      saveToken(user.getUsername(), accessToken);
      signInResponse.setEmailAddress(user.getEmailAddress());
      signInResponse.setAccessToken(accessToken);
      signInResponse.setRefreshToken(refreshToken);
      return signInResponse;
    }

    if (user.isMfaEnabled()) {
      if (SMS == user.getMfaType() || EMAIL == user.getMfaType()) {
        String otp = mfaService.generateVerificationOtp(6);
        PreVerificationDto preVerification = PreVerificationDto.builder()
                .code(otp)
                .phoneNumber(user.getPhoneNumber())
                .emailAddress(dto.getEmailAddress())
                .build();

        VerificationType verificationType = VerificationType.valueOf(user.getMfaType().name());
        initPreVerification(preVerification, verificationType);
      }

      user.setAuthorities(getPreAuthenticatedAuthorities());
      Authentication authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
      accessToken = createAccessToken(user);
      refreshToken = createRefreshToken(user);
      setContext(authenticationToken);

      signInResponse.setMfaEnabled(true);
      signInResponse.setMfaType(user.getMfaType());
      signInResponse.setAccessToken(accessToken);
      signInResponse.setRefreshToken(refreshToken);
      signInResponse.setPhoneNumber(user.getPhoneNumber());

      saveToken(user.getUsername(), accessToken);
      return signInResponse;
    }

    accessToken = createAccessToken(user);
    refreshToken = createRefreshToken(user);
    setContext(authentication);

    signInResponse.setAccessToken(accessToken);
    signInResponse.setRefreshToken(refreshToken);
    signInResponse.setAuthenticationStatus(COMPLETED);

    saveToken(user.getUsername(), accessToken);
    return signInResponse;
  }

  @Override
  @Transactional
  public SignUpResponse signUp(SignUpDto dto) {
    Member member = dto.toMember();

    Role role = roleService.getRoleByCode(PRE_VERIFIED_USER.name());
    member.setPassword(passwordEncoder.encode(member.getPassword().trim()));
    member.setRoles(Set.of(role));

    MemberStatus memberStatus = memberStatusService.getMemberStatusByCode(MemberStatusType.INACTIVE.name());
    member.setMemberStatus(memberStatus);
    memberService.save(member);

    FleenUser user = initAuthentication(member);
    String accessToken = createAccessToken(user);
    String refreshToken = createRefreshToken(user);

    String otp = mfaService.generateVerificationOtp(6);
    PreVerificationDto preVerification = PreVerificationDto.builder()
            .code(otp)
            .phoneNumber(dto.getPhoneNumber())
            .emailAddress(dto.getEmailAddress())
            .build();

    VerificationType verificationType = VerificationType.valueOf(dto.getVerificationType());
    initPreVerification(preVerification, verificationType);
    cacheService.set(getPreVerificationCacheKey(user.getUsername()), otp, Duration.ofMinutes(1));

    saveToken(user.getUsername(), accessToken);
    return new SignUpResponse(accessToken, refreshToken, IN_PROGRESS, verificationType);
  }

  @Override
  @Transactional
  public SignUpResponse completeSignUp(VerificationCodeDto dto, FleenUser fleenUser) {
    String username = fleenUser.getUsername();
    String verificationKey = getPreVerificationCacheKey(username);
    String code = dto.getCode();

    validateSmsAndEmailMfa(verificationKey, code);

    Member member = memberService.getMemberByEmailAddress(username);
    if (Objects.isNull(member)) {
      throw new VerificationFailedException();
    }

    FleenUser user = initAuthentication(member);
    String accessToken = createAccessToken(user);
    String refreshToken = createAccessToken(user);

    if (MemberStatusType.ACTIVE.getValue().equals(member.getMemberStatus().getCode())) {
      saveToken(user.getUsername(), accessToken);
      return new SignUpResponse(accessToken, refreshToken, COMPLETED, null);
    }

    Role role = roleService.getRoleByCode(RoleType.USER.name());
    Set<Role> roles = new HashSet<>();
    roles.add(role);
    member.setRoles(roles);

    MemberStatus memberStatus = memberStatusService.getMemberStatusByCode(MemberStatusType.ACTIVE.name());
    member.setMemberStatus(memberStatus);
    memberService.save(member);

    saveToken(user.getUsername(), accessToken);
    return new SignUpResponse(accessToken, refreshToken, COMPLETED, null);
  }

  @Override
  public FleenHealthResponse resendVerificationCode(ResendVerificationCodeDto dto, FleenUser user) {
    String username = user.getUsername();
    String verificationKey = getPreVerificationCacheKey(username);
    if (cacheService.exists(verificationKey)) {
      throw new VerificationFailedException();
    }

    VerificationType verificationType = VerificationType.valueOf(dto.getVerificationType());
    String otp = mfaService.generateVerificationOtp(6);
    PreVerificationDto preVerification = PreVerificationDto.builder()
            .code(otp)
            .phoneNumber(dto.getPhoneNumber())
            .emailAddress(dto.getEmailAddress())
            .build();

    initPreVerification(preVerification, verificationType);
    cacheService.set(getPreVerificationCacheKey(user.getUsername()), otp, Duration.ofMinutes(5));
    return new FleenHealthResponse(VERIFICATION_CODE_MESSAGE);
  }

  public FleenUser initAuthentication(Member member) {
    FleenUser user = FleenUser.fromMember(member);
    Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    setContext(authentication);
    return user;
  }

  @Override
  public void signOut(String username) {
    String key = getAuthCacheKey(username);
    if (cacheService.exists(key)) {
      cacheService.delete(AUTH_CACHE_PREFIX.concat(username));
    }
  }

  @Override
  public SignInResponse validateMfa(FleenUser fleenUser, ConfirmMfaDto dto) {
    String username = fleenUser.getUsername();
    MfaType mfaType = MfaType.valueOf(dto.getMfaType());
    String code = dto.getCode();

    if (SMS == mfaType || EMAIL == mfaType) {
      String verificationKey = getPreVerificationCacheKey(username);
      validateSmsAndEmailMfa(verificationKey, code);
    }

    Member member = memberService.getMemberByEmailAddress(username);
    if (Objects.isNull(member)) {
      throw new VerificationFailedException();
    }

    FleenUser user = initAuthentication(member);
    String accessToken = createAccessToken(user);
    String refreshToken = createRefreshToken(user);

    String secret = memberService.getTwoFaSecret(fleenUser.getId());
    mfaService.verifyOtp(code, secret);
    return SignInResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .authenticationStatus(COMPLETED)
            .build();
  }

  private void validateSmsAndEmailMfa(String verificationKey, String code) {
    if (!cacheService.exists(verificationKey)) {
      throw new ExpiredVerificationCodeException();
    }

    if (!cacheService.get(verificationKey).equals(code)) {
      throw new InvalidVerificationCodeException(code);
    }
  }

  @Override
  public String getLoggedInUserEmailAddress() {
    Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
    if (userDetails instanceof FleenUser) {
      return ((UserDetails) userDetails).getUsername();
    }
    return null;
  }

  @Override
  public Authentication authenticate(String emailAddress, String password) {
    Authentication authenticationToken =
            new UsernamePasswordAuthenticationToken(emailAddress.trim().toLowerCase(), password);

    return authenticationManager.authenticate(authenticationToken);
  }

  @Override
  public void setContext(Authentication authentication) {
    if (authentication.isAuthenticated()) {
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
  }

  @Override
  public SignInResponse refreshToken(String username) {
    signOut(username);
    FleenUser user = (FleenUser) userDetailsService.loadUserByUsername(username);

    String accessToken = createAccessToken(user);
    String refreshToken = createRefreshToken(user);
    Authentication authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    setContext(authenticationToken);

    saveToken(username, accessToken);
    return SignInResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .emailAddress(user.getEmailAddress())
            .phoneNumber(user.getPhoneNumber())
            .authenticationStatus(COMPLETED)
            .build();
  }

  @Override
  public String createAccessToken(FleenUser user) {
    return jwtProvider.generateToken(user, ACCESS_TOKEN);
  }

  @Override
  public String createRefreshToken(FleenUser user) {
    return jwtProvider.generateRefreshToken(user, REFRESH_TOKEN);
  }

  @Override
  public void saveToken(String subject, String token) {
    Duration duration = Duration.ofHours(toHours(new Date(), jwtProvider.getExpirationDateFromToken(token)));
    cacheService.set(getAuthCacheKey(subject), token, duration);
  }

  public static String getAuthCacheKey(String username) {
    return AUTH_CACHE_PREFIX.concat(username);
  }

  public static String getPreVerificationCacheKey(String username) {
    return PRE_VERIFICATION_PREFIX.concat(username);
  }

  private String getPreVerificationTemplate(Map<String, Object> data) {
    return emailService.processAndReturnTemplate(PRE_VERIFICATION_TEMPLATE_NAME, data);
  }

  private void initPreVerification(PreVerificationDto dto, VerificationType verificationType) {
    if (Objects.requireNonNull(verificationType) == VerificationType.SMS) {
      sendSmsPreVerificationCode(dto);
    } else {
      sendEmailPreVerificationCode(dto);
    }
  }

  private void sendEmailPreVerificationCode(PreVerificationDto dto) {
    String emailBody = getPreVerificationTemplate(Map.of(PRE_VERIFICATION_OTP_KEY, dto.getCode()));
    if (emailBody == null) {
      throw new VerificationFailedException();
    }
    EmailDetails emailDetails = EmailDetails.builder()
            .from(EmailMessageSource.BASE.getValue())
            .to(dto.getEmailAddress())
            .subject(PRE_VERIFICATION_EMAIL_SUBJECT)
            .body(emailBody)
            .build();
    emailService.sendHtmlMessage(emailDetails);
  }

  private void sendSmsPreVerificationCode(PreVerificationDto dto) {
    Optional<SmsMessage> verificationTemplate = mobileTextService.getPreVerificationSmsMessage(MessageType.PRE_VERIFICATION);
    if (verificationTemplate.isEmpty()) {
      throw new VerificationFailedException();
    }
    String verificationMessage = MessageFormat.format(verificationTemplate.get().getMessage(), dto.getCode());
    mobileTextService.sendSms(dto.getPhoneNumber(), verificationMessage);
  }
}
