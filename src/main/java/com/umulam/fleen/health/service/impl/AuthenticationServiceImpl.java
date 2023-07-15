package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.adapter.google.recaptcha.ReCaptchaAdapter;
import com.umulam.fleen.health.adapter.google.recaptcha.model.response.ReCaptchaResponse;
import com.umulam.fleen.health.constant.CommonEmailMessageTemplateDetails;
import com.umulam.fleen.health.constant.MemberStatusType;
import com.umulam.fleen.health.constant.VerificationMessageType;
import com.umulam.fleen.health.constant.authentication.MfaType;
import com.umulam.fleen.health.constant.authentication.NextAuthentication;
import com.umulam.fleen.health.constant.authentication.RoleType;
import com.umulam.fleen.health.constant.authentication.VerificationType;
import com.umulam.fleen.health.constant.base.ProfileType;
import com.umulam.fleen.health.constant.verification.ProfileVerificationMessageType;
import com.umulam.fleen.health.constant.verification.ProfileVerificationStatus;
import com.umulam.fleen.health.exception.authentication.*;
import com.umulam.fleen.health.exception.member.UserNotFoundException;
import com.umulam.fleen.health.model.domain.*;
import com.umulam.fleen.health.model.dto.authentication.*;
import com.umulam.fleen.health.model.request.CompleteUserSignUpRequest;
import com.umulam.fleen.health.model.request.PreVerificationOrAuthenticationRequest;
import com.umulam.fleen.health.model.request.SaveProfileVerificationMessageRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static com.umulam.fleen.health.constant.authentication.AuthenticationConstant.*;
import static com.umulam.fleen.health.constant.authentication.AuthenticationStatus.COMPLETED;
import static com.umulam.fleen.health.constant.authentication.AuthenticationStatus.IN_PROGRESS;
import static com.umulam.fleen.health.constant.authentication.MfaType.*;
import static com.umulam.fleen.health.constant.authentication.RoleType.*;
import static com.umulam.fleen.health.constant.authentication.TokenType.ACCESS_TOKEN;
import static com.umulam.fleen.health.constant.authentication.TokenType.REFRESH_TOKEN;
import static com.umulam.fleen.health.constant.base.FleenHealthConstant.VERIFICATION_CODE_SENT_MESSAGE;
import static com.umulam.fleen.health.util.DateTimeUtil.addMinutesFromNow;
import static com.umulam.fleen.health.util.DateTimeUtil.toHours;
import static com.umulam.fleen.health.util.FleenAuthorities.*;
import static java.util.Objects.nonNull;

@Slf4j
@Service
public class AuthenticationServiceImpl implements
        AuthenticationService,
        CommonAuthAndVerificationService,
        PasswordService {

  private final AuthenticationManager authenticationManager;
  private final MemberService memberService;
  private final MemberStatusService memberStatusService;
  private final RoleService roleService;
  private final MfaService mfaService;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;
  private final CacheService cacheService;
  private final MobileTextService mobileTextService;
  private final EmailServiceImpl emailService;
  private final VerificationHistoryService verificationHistoryService;
  private final ProfileVerificationMessageService profileVerificationMessageService;
  private final ProfileTokenService profileTokenService;
  private final ReCaptchaAdapter reCaptchaAdapter;

  public AuthenticationServiceImpl(AuthenticationManager authenticationManager,
                                   MemberService memberService,
                                   MemberStatusService memberStatusService,
                                   RoleService roleService,
                                   MfaService mfaService,
                                   PasswordEncoder passwordEncoder,
                                   JwtProvider jwtProvider,
                                   CacheService cacheService,
                                   MobileTextService mobileTextService,
                                   EmailServiceImpl emailService,
                                   VerificationHistoryService verificationHistoryService,
                                   ProfileVerificationMessageService profileVerificationMessageService,
                                   ProfileTokenService profileTokenService,
                                   ReCaptchaAdapter reCaptchaAdapter) {
    this.authenticationManager = authenticationManager;
    this.memberService = memberService;
    this.memberStatusService = memberStatusService;
    this.roleService = roleService;
    this.mfaService = mfaService;
    this.passwordEncoder = passwordEncoder;
    this.jwtProvider = jwtProvider;
    this.cacheService = cacheService;
    this.mobileTextService = mobileTextService;
    this.emailService = emailService;
    this.verificationHistoryService = verificationHistoryService;
    this.profileVerificationMessageService = profileVerificationMessageService;
    this.profileTokenService = profileTokenService;
    this.reCaptchaAdapter = reCaptchaAdapter;
  }

  /**
   * <p>How the user sign in to get a access and refresh token to use to send requests to and access features of the application or API. The process
   * of validating the credentials i.e. the email address and password is handled by the {@link #authenticate(String, String) authenticate} method.</p>
   * <br/>
   *
   * <p>An entity's (user) authentication status can be {@link com.umulam.fleen.health.constant.authentication.AuthenticationStatus#IN_PROGRESS In Progress}
   * for several reasons for example when the user's profile is yet to complete the registration process initiated at {@link #signUp(SignUpDto) signUp} or be
   * activated. Another reason could be because the user profile has a Multi-Factor Authentication (MFA_OR_PRE_AUTHENTICATION) enabled. This status works in tandem with
   * {@link NextAuthentication} so that the consumer of the API would be able to know what other authentication needs to be completed and decide what view or
   * UI to display.</p>
   * <br/>
   *
   * <p>A user is not allowed to access the application features or functionalities if their account or profile is disabled.</p>
   * <br/>
   *
   * <p>If the user has completed the registration and has no MFA_OR_PRE_AUTHENTICATION enabled on their profile, the authentication status is assumed to be completed and
   * and will be allowed to access and use the features of the application as allowed and specified by the role associated with their profile.</p>
   * <br/>
   *
   * <p>When a profile is yet to be activated or the user has not finished the registration process, A user is required to complete a verification process.
   * By default, a One Time Password (OTP) generated through {@link MfaService#generateVerificationOtp(int) generateVerificationOtp} and saved through
   * {@link #savePreVerificationOtp(String, String) savePreVerificationOtp} is sent to the user's registered email address which will be used to complete
   * the registration or sign up at {@link #completeSignUp(VerificationCodeDto, FleenUser) completeSignUp}.</p>
   * <br/>
   *
   * <p>During the login or sign-in process, a access and refresh token with predefined authorities or roles is registered in the authentication context
   * of the user and will be replaced with the original authorities in the DB or store associated with the user's profile after successfully completing
   * the authentication and its stages at {@link com.umulam.fleen.health.controller.VerificationController#validateSignInMfa(FleenUser, ConfirmMfaDto)} (FleenUser, ConfirmMfaDto) validateSignInMfa}.
   * This predefined authorities assigned to the authentication context during the sign-in process is to make sure that the user is not allowed to perform
   * actions or access security protected features of the application if the authentication process is yet to be completed.</p>
   * <br/>
   *
   * <p>After verifying the credentials of the user, a session like context is initialized for the user using {@link #cacheService CacheService} through
   * {@link #saveToken(String, String) saveToken}. The existence of this session and user authentication context will be validated on each request that is sent
   * to the API if the request URL is one that only authenticated user can access or the
   * {@link org.springframework.http.HttpHeaders#AUTHORIZATION Authorization} header is present in the request, the request will usually pass through the
   * {@link com.umulam.fleen.health.filter.JwtAuthenticationFilter#doFilter(ServletRequest, ServletResponse, FilterChain) JwtAuthenticationFilter}.</p>
   * <br/>
   *
   * <p>In cases where the MFA_OR_PRE_AUTHENTICATION enabled on the user's profile is type {@link VerificationType#SMS SMS} or {@link VerificationType#EMAIL EMAIL}. A OTP is sent to
   * the email address or phone number found on their profile, the process of delivering the SMS or EMAIL message is handled and performed by
   * {@link #sendVerificationMessage(PreVerificationOrAuthenticationRequest, VerificationType) sendVerificationMessage}.</p>
   * <br/>
   *
   * <p>When MFA_OR_PRE_AUTHENTICATION is enabled on the profile, setting the refresh token to 'NULL' is important and necessary else if the refresh token is set, the user will be able to
   * get a new access token that contains the complete details of the user including the authorities that can be used
   * to access the application features or perform actions on the API without completing the authentication process if MFA_OR_PRE_AUTHENTICATION is enabled on the user's profile.</p>
   * <br/>
   *
   * <p>Other details are set on the {@link SignInResponse} object to allow the consumer of the API or response decide the next action or process to
   * perform. For example, if the user has {@link MfaType MFA_OR_PRE_AUTHENTICATION} enabled on their profile; they will have to submit a code or OTP sent to their
   * email address or phone number or get the code or OTP from an Authenticator app such as Google or Microsoft Authenticator which is going
   * to be used to complete the authentication process they initiated or started.</p>
   * <br/>
   *
   * @param dto contains a possible user-registered email address and password
   * @return {@link SignInResponse} if the authentication and validation of the user credential is successful.
   * Also contains access token that can be used to access features of the application or API and also the
   * refresh token to use to get a new access token when the previous access token has expired.
   * @throws InvalidAuthenticationException if the authentication and validation of the user credential is unsuccessful
   */
  @Override
  public SignInResponse signIn(SignInDto dto) {
    Authentication authentication = authenticate(dto.getEmailAddress(), dto.getPassword());
    if (!authentication.isAuthenticated()) {
      throw new InvalidAuthenticationException(dto.getEmailAddress());
    }

    FleenUser user = (FleenUser) authentication.getPrincipal();
    Role role = user.authoritiesToRoles().get(0);
    String accessToken;
    String refreshToken;

    if (MemberStatusType.DISABLED.name().equals(user.getStatus())) {
      throw new DisabledAccountException();
    }

    SignInResponse signInResponse = SignInResponse
            .builder()
            .emailAddress(user.getEmailAddress())
            .authenticationStatus(IN_PROGRESS)
            .nextAuthentication(NextAuthentication.NONE)
            .mfaEnabled(false)
            .build();
    RoleType roleType = RoleType.valueOf(role.getCode());

    if (MemberStatusType.INACTIVE.name().equals(user.getStatus())
        && (PRE_ONBOARDED == roleType)) {
      user.setAuthorities(getUserPreOnboardedAuthorities());
      initSignInDetails(user, signInResponse);

      signInResponse.setNextAuthentication(NextAuthentication.PRE_ONBOARDED);
      return signInResponse;
    }

    if (MemberStatusType.INACTIVE.name().equals(user.getStatus())
        && (PRE_VERIFIED_USER == roleType ||
            PRE_VERIFIED_PROFESSIONAL == roleType ||
            PRE_VERIFIED_BUSINESS == roleType)) {
      String otp = generateOtp();
      PreVerificationOrAuthenticationRequest request = createPreVerificationRequest(otp, user);

      sendVerificationMessage(request, VerificationType.EMAIL);
      savePreVerificationOtp(user.getUsername(), otp);

      setPreVerificationAuthorities(user, roleType);
      initSignInDetails(user, signInResponse);

      signInResponse.setNextAuthentication(NextAuthentication.PRE_VERIFICATION);
      return signInResponse;
    }

    if (user.isMfaEnabled()) {
      if (SMS == user.getMfaType() || EMAIL == user.getMfaType()) {
        VerificationType verificationType = VerificationType.valueOf(user.getMfaType().name());
        String otp = generateOtp();
        PreVerificationOrAuthenticationRequest request = createPreAuthenticationRequest(otp, user);

        sendVerificationMessage(request, verificationType);
        savePreAuthenticationOtp(user.getUsername(), otp);
      }

      user.setAuthorities(getPreAuthenticatedAuthorities());
      Authentication authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
      accessToken = createAccessToken(user);

      setContext(authenticationToken);
      saveToken(user.getUsername(), accessToken);

      signInResponse.setNextAuthentication(NextAuthentication.MFA_OR_PRE_AUTHENTICATION);
      signInResponse.setMfaEnabled(true);
      signInResponse.setMfaType(user.getMfaType());
      signInResponse.setAccessToken(accessToken);
      signInResponse.setRefreshToken(null);
      signInResponse.setPhoneNumber(user.getPhoneNumber());
      return signInResponse;
    }

    accessToken = createAccessToken(user);
    refreshToken = createRefreshToken(user);

    setContext(authentication);
    saveToken(user.getUsername(), accessToken);
    saveRefreshToken(user.getUsername(), refreshToken);

    signInResponse.setAccessToken(accessToken);
    signInResponse.setRefreshToken(refreshToken);
    signInResponse.setAuthenticationStatus(COMPLETED);
    return signInResponse;
  }

  /**
   * <p>How the user register on the application or API.</p>
   * <br/>
   *
   * <p>Necessary validation checks for example if the email address or phone number has been used by another user is performed on the dto {@link SignUpDto}
   * and this process is aided by the {@link com.umulam.fleen.health.validator.EmailAddressExists EmailAddressExists} and
   * {@link com.umulam.fleen.health.validator.PhoneNumberExists PhoneNumberExists} annotations.</p>
   * <br/>
   *
   * <p>The {@link ProfileType} will be used to decide what role to associate with the user's profile as a user or professional or business.</p>
   * <br/>
   *
   * <p>The password chosen by the user would be encoded through an algorithm for example using
   * {@link org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder BCryptPasswordEncoder}.</p>
   * <br/>
   *
   * <p>If the registration completes, a {@link ProfileVerificationHistory} entry is created that informs the user their profile verification is pending. An
   * email message will be sent to the registered email address informing the user of this process and status. By default, a user profile will be
   * 'inactive' during sign up until the registration process has been completed at {@link #completeSignUp(VerificationCodeDto, FleenUser) completeSignUp}.
   * The user will be able to view a history of the stages their profile is currently in and has passed through.</p>
   * <br/>
   *
   * <p>The user will complete the registration process by using a OTP delivered to their phone number or email address through
   * {@link #sendVerificationMessage(PreVerificationOrAuthenticationRequest, VerificationType) sendVerificationMessage}</p>
   * <br/>
   *
   * @param dto contains basic details that allow a registration on the platform
   * @return {@link SignUpResponse} that contains other details like access token,
   * refresh token and authentication progress that can be used to complete
   * the registration process.
   */
  @Override
  @Transactional
  public SignUpResponse signUp(SignUpDto dto) {
    Member member = dto.toMember();
    ProfileType userType = ProfileType.valueOf(dto.getProfileType());

    Role role = setNewUserRole(userType);
    initNewUserDetails(member, List.of(role));

    ProfileVerificationStatus verificationStatus = ProfileVerificationStatus.PENDING;
    member.setVerificationStatus(verificationStatus);
    member.setUserType(userType);
    member.setPassword(createEncodedPassword(member.getPassword()));
    memberService.save(member);

    FleenUser user = initAuthentication(member);
    String accessToken = createAccessToken(user);
    String refreshToken = createRefreshToken(user);

    String otp = generateOtp();
    PreVerificationOrAuthenticationRequest request = createPreVerificationRequest(otp, user);

    VerificationType verificationType = VerificationType.valueOf(dto.getVerificationType());
    sendVerificationMessage(request, verificationType);
    savePreVerificationOtp(user.getUsername(), otp);

    createProfileVerificationMessageNewPendingRegistration(member);

    saveToken(user.getUsername(), accessToken);
    saveRefreshToken(user.getUsername(), refreshToken);
    return SignUpResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .authenticationStatus(IN_PROGRESS)
            .verificationType(verificationType)
            .emailAddress(member.getEmailAddress())
            .phoneNumber(member.getPhoneNumber())
            .build();
  }

  /**
   * <p>How the complete the registration process on the application or API.</p>
   * <br/>
   *
   * <p>The OTP sent to the user's email address or phone number will be validated or verified of existence and equality.</p>
   * <br/>
   *
   * <p>If the profile type is {@link ProfileType#USER USER}, the verification is approved automatically because they have a single verification stage.
   * This does not applies to a business or professional because they have to upload other documents like medical certificate, licenses, business registration
   * certificate and in some cases tax identification certificate if deemed necessary.</p>
   * <br/>
   *
   * <p>All profiles are set to active using {@link MemberStatusType#ACTIVE Active} if the registration process is completed successfully.</p>
   *
   * @param dto contains verification code to use to complete the user registration process
   * @param user the authenticated user after the user successfully started the registration or sign-up process.
   * @return {@link SignUpResponse} contains access token and refresh token that can be used to access the application or API feature
   * @throws VerificationFailedException if the user's email address does not exist
   * @throws ExpiredVerificationCodeException if the OTP has already expired and is deleted from the cache at {@link CacheService}
   * @throws InvalidVerificationCodeException if the OTP is not equal to what is currently available in the cache at {@link CacheService}
   * @throws AlreadySignedUpException if the registration process has been completed using this email address
   */
  @Override
  @Transactional
  public SignUpResponse completeSignUp(VerificationCodeDto dto, FleenUser user) {
    String username = user.getUsername();
    String verificationKey = getPreVerificationCacheKey(username);
    VerificationType verificationType = VerificationType.valueOf(dto.getVerificationType());
    String code = dto.getCode();

    validateSmsAndEmailVerificationCode(verificationKey, code);

    Member member = memberService.getMemberByEmailAddress(username);
    if (Objects.isNull(member)) {
      throw new VerificationFailedException();
    }

    FleenUser freshUser = initAuthentication(member);
    String accessToken = createAccessToken(freshUser);
    String refreshToken = createRefreshToken(freshUser);

    if (MemberStatusType.ACTIVE.getValue().equals(member.getMemberStatus().getCode())) {
      throw new AlreadySignedUpException();
    }

    CompleteUserSignUpRequest request = createCompleteUserSignUpRequest(member);
    Role role = request.getRole();
    ProfileVerificationMessage verificationMessage = request.getVerificationMessage();
    ProfileVerificationStatus profileVerificationStatus = request.getProfileVerificationStatus();

    if (nonNull(verificationMessage)) {
      SaveProfileVerificationMessageRequest verificationMessageRequest = SaveProfileVerificationMessageRequest.builder()
              .verificationMessageType(verificationMessage.getVerificationMessageType())
              .verificationStatus(profileVerificationStatus)
              .member(member)
              .emailAddress(member.getEmailAddress())
              .build();

      saveProfileVerificationHistory(verificationMessage, verificationMessageRequest);
      sendProfilePreVerificationMessage(member.getEmailAddress(), verificationMessage);
    }

    Set<Role> roles = new HashSet<>();
    roles.add(role);
    member.setRoles(roles);

    if (verificationType == VerificationType.SMS) {
      member.setPhoneNumberVerified(true);
    } else {
      member.setEmailAddressVerified(true);
    }

    MemberStatus memberStatus = memberStatusService.getMemberStatusByCode(MemberStatusType.ACTIVE.name());
    member.setMemberStatus(memberStatus);
    memberService.save(member);

    clearPreVerificationOtp(freshUser.getUsername());
    saveToken(freshUser.getUsername(), accessToken);
    saveRefreshToken(freshUser.getUsername(), refreshToken);
    return SignUpResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .authenticationStatus(COMPLETED)
            .verificationType(null)
            .emailAddress(null)
            .phoneNumber(null)
            .build();
  }

  /**
   * <p>User can request for a new OTP if they are unable to complete their registration for example because they abandoned the sign-up process midway or because
   * the OTP they want to use to complete the registration at {@link #completeSignUp(VerificationCodeDto, FleenUser) completeSignUp} has expired.</p>
   * <br/>
   *
   * <p>Where the OTP will be sent will be decided by the {@link ResendVerificationCodeDto#getVerificationType() verificationType} and this can be through
   * {@link VerificationType#EMAIL EMAIL} or {@link VerificationType#SMS SMS}.</p>
   * <br/>
   *
   * @param dto contains phone number or email address to send OTP to, to complete the verification process
   * @param user the user wanting to complete the verification process
   * @return {@link FleenHealthResponse} if the code has been sent successfully
   * @throws VerificationFailedException if there's already an existing OTP associated with the user profile
   */
  @Override
  public FleenHealthResponse resendPreVerificationCode(ResendVerificationCodeDto dto, FleenUser user) {
    VerificationType verificationType = VerificationType.valueOf(dto.getVerificationType());
    String otp = generateOtp();
    PreVerificationOrAuthenticationRequest preVerification = createPreVerificationRequest(otp, user);

    sendVerificationMessage(preVerification, verificationType);
    savePreVerificationOtp(user.getUsername(), otp);
    return new FleenHealthResponse(VERIFICATION_CODE_SENT_MESSAGE);
  }

  /**
   * <p>User can request for a new OTP if they are unable to complete the sign-in for example because the OTP they want to use to complete the sign-in
   * {@link #validateSignInMfa(FleenUser, ConfirmMfaDto) validateSignInMfa} has expired.</p>
   * <br/>
   *
   * <p>Where the OTP will be sent will be decided by the {@link ResendVerificationCodeDto#getVerificationType() verificationType} and this can be through
   * {@link VerificationType#EMAIL EMAIL} or {@link VerificationType#SMS SMS}.</p>
   * <br/>
   *
   * @param dto contains phone number or email address to send OTP to, to complete the verification process
   * @param user the user wanting to complete the verification process
   * @return {@link FleenHealthResponse} if the code has been sent successfully
   * @throws VerificationFailedException if there's already an existing OTP associated with the user profile
   */
  @Override
  public FleenHealthResponse resendPreAuthenticationCode(ResendVerificationCodeDto dto, FleenUser user) {
    VerificationType verificationType = VerificationType.valueOf(dto.getVerificationType());
    String otp = generateOtp();
    PreVerificationOrAuthenticationRequest preVerification = createPreAuthenticationRequest(otp, user);

    sendVerificationMessage(preVerification, verificationType);
    savePreAuthenticationOtp(user.getUsername(), otp);
    return new FleenHealthResponse(VERIFICATION_CODE_SENT_MESSAGE);
  }

  /**
   * Initialize the authentication context with the user details
   * @param member the registered user to associate with the authentication context
   * @return {@link FleenUser} a user that been associated with the authentication context
   */
  public FleenUser initAuthentication(Member member) {
    FleenUser user = FleenUser.fromMember(member);
    Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    setContext(authentication);
    return user;
  }

  /**
   * Remove the user authentication context token from the cache
   * @param username the name of a user associated with the authentication context
   */
  @Override
  public void signOut(String username) {
    String accessAuthKey = getAuthCacheKey(username);
    String refreshAuthKey = getAuthRefreshCacheKey(username);

    if (cacheService.exists(accessAuthKey)) {
      cacheService.delete(accessAuthKey);
    }

    if (cacheService.exists(refreshAuthKey)) {
      cacheService.delete(refreshAuthKey);
    }
    SecurityContextHolder.getContext().setAuthentication(null);
    SecurityContextHolder.clearContext();
  }

  /**
   * <p>When a user has {@link MfaType MFA_OR_PRE_AUTHENTICATION} enabled on their profile, they are required to pass through this process before they are fully authenticated and
   * can access the feature of the application as designated by their roles.</p>
   * <br/>
   *
   * <p>The {@link ConfirmMfaDto#getMfaType() MfaType} will be used to decide what service to deliver the and validate the OTP or code against. For example, if the user
   * has enabled the {@link MfaType#AUTHENTICATOR Authenticator} type of MFA_OR_PRE_AUTHENTICATION on their profile, the code will be validated through
   * {@link MfaService#verifyAuthenticatorOtp(String, String) verifyAuthenticatorOtp} by retrieving the secret associated with the user's profile. If the MFA_OR_PRE_AUTHENTICATION type is for example
   * {@link MfaType#EMAIL EMAIL}, the code will be validated through {@link #validateMfaSetupCode(String, String)} (String, String) validateSmsAndEmailMfa}.</p>
   * <br/>
   *
   * @param fleenUser The user that has started the sign-in process at {@link #signIn(SignInDto) signIn}.
   * @param dto details including the OTP code and type required to complete the sign-in process
   * @return {@link SignInResponse} if the validation was successful
   * @throws VerificationFailedException if the user's email address does not exist
   * @throws ExpiredVerificationCodeException if the OTP has already expired and is deleted from the cache at {@link CacheService#exists(String) exists}
   * @throws InvalidVerificationCodeException if the OTP is not equal to what is currently available in the cache at {@link CacheService#get(String) get}
   */
  @Override
  @Transactional(readOnly = true)
  public SignInResponse validateSignInMfa(FleenUser fleenUser, ConfirmMfaDto dto) {
    String username = fleenUser.getUsername();
    MfaType mfaType = MfaType.valueOf(dto.getMfaType());
    String code = dto.getCode();

    if (SMS == mfaType || EMAIL == mfaType) {
      String verificationKey = getPreAuthenticationCacheKey(username);
      validateSmsAndEmailVerificationCode(verificationKey, code);
    }
    else if (AUTHENTICATOR == mfaType) {
      String secret = memberService.getTwoFaSecret(fleenUser.getId());
      boolean valid = mfaService.verifyAuthenticatorOtp(code, secret);
      if (!valid) {
        throw new InvalidVerificationCodeException(code);
      }
    }

    Member member = memberService.getMemberByEmailAddress(username);
    if (Objects.isNull(member)) {
      throw new InvalidAuthenticationException(username);
    }

    FleenUser user = initAuthentication(member);
    String accessToken = createAccessToken(user);
    String refreshToken = createRefreshToken(user);

    clearPreAuthenticationOtp(username);
    saveToken(username, accessToken);
    saveRefreshToken(username, refreshToken);
    return SignInResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .authenticationStatus(COMPLETED)
            .build();
  }

  /**
   * <p>Authenticate the user's credentials before granting them access to the application or API features.</p>
   * <br/>
   *
   * @param emailAddress a user identifier to validate against during the authentication process
   * @param password a password to validate and match with the encoded password stored in the DB
   * @return {@link Authentication} after the authentication process is completed
   */
  @Override
  public Authentication authenticate(String emailAddress, String password) {
    Authentication authenticationToken =
            new UsernamePasswordAuthenticationToken(emailAddress.trim().toLowerCase(), password);
    return authenticationManager.authenticate(authenticationToken);
  }

  /**
   * <p>Associate the user's authentication with the Security Context using {@link SecurityContextHolder}</p>
   * <br/>
   *
   * @param authentication the authenticated user
   */
  @Override
  public void setContext(Authentication authentication) {
    if (authentication.isAuthenticated()) {
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
  }

  /**
   * <p>When a user signs up but has not completed the registration process. The user will be required to do so through
   * {@link #completeSignUp(VerificationCodeDto, FleenUser) completeSignUp} and will be associated with appropriate authorities through
   * {@link com.umulam.fleen.health.util.FleenAuthorities FleenAuthorities}</p>
   * <br/>
   *
   * @param user the authenticated user
   * @param roleType the role of the user that will be used to decide the authorities to associate with the user during the authentication process
   */
  private void setPreVerificationAuthorities(FleenUser user, RoleType roleType) {
    switch (roleType) {
      case PRE_VERIFIED_USER:
        user.setAuthorities(getUserPreVerifiedAuthorities());
        break;

      case PRE_VERIFIED_PROFESSIONAL:
        user.setAuthorities(getProfessionalPreVerifiedAuthorities());
        break;

      case PRE_VERIFIED_BUSINESS:
        user.setAuthorities(getBusinessPreVerifiedAuthorities());
        break;
    }
  }

  /**
   * <p>Generate a new access token that can be used to access the application or API features.</p>
   * <br/>
   *
   * @param username the user's identifier associated with the access token
   * @return {@link SignInResponse} that contains new access token
   * that the user can use to perform actions on the application
   */
  @Override
  @Transactional(readOnly = true)
  public SignInResponse refreshToken(String username, String token) {
    String verificationKey = getAuthRefreshCacheKey(username);

    if (!cacheService.exists(verificationKey)) {
      throw new InvalidAuthenticationToken();
    }

    if (!cacheService.get(verificationKey).equals(token)) {
      throw new InvalidAuthenticationToken();
    }

    signOut(username);
    Member member = memberService.getMemberByEmailAddress(username);
    if (Objects.isNull(member)) {
      throw new InvalidAuthenticationException(username);
    }

    FleenUser user = FleenUser.fromMember(member);
    String accessToken = createAccessToken(user);
    String refreshToken = createRefreshToken(user);
    Authentication authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    setContext(authenticationToken);

    saveToken(username, accessToken);
    saveRefreshToken(username, refreshToken);
    return SignInResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .emailAddress(user.getEmailAddress())
            .phoneNumber(user.getPhoneNumber())
            .authenticationStatus(COMPLETED)
            .nextAuthentication(NextAuthentication.NONE)
            .build();
  }

  /**
   * <p>Create a access token that can be used to perform actions on the application or through the API.</p>
   * <br/>
   *
   * @param user the authenticated user
   * @return the token to use to access the application or API features
   */
  @Override
  public String createAccessToken(FleenUser user) {
    return jwtProvider.generateToken(user, ACCESS_TOKEN);
  }

  /**
   * <p>Create a refresh token that can be used to obtain new access token to perform actions on the application or through the API.</p>
   * <br/>
   *
   * @param user the authenticated user
   * @return the token to use to obtain new access token.
   */
  @Override
  public String createRefreshToken(FleenUser user) {
    return jwtProvider.generateRefreshToken(user, REFRESH_TOKEN);
  }

  /**
   * <p>Save the authentication access token.</p>
   * <br/>
   *
   * @param subject the user's identifier to associate with the access token
   * @param token the user's token to validate during the requests and process of using the application
   */
  @Override
  public void saveToken(String subject, String token) {
    Duration duration = Duration.ofHours(toHours(new Date(), jwtProvider.getExpirationDateFromToken(token)));
    cacheService.set(getAuthCacheKey(subject), token, duration);
  }

  /**
   * <p>Save the authentication refresh token that can only be used once to get a new access token.</p>
   * <br/>
   *
   * @param subject the user's identifier to associate with the refresh token
   * @param token the user's token to validate during request and use to get a new token
   */
  @Override
  public void saveRefreshToken(String subject, String token) {
    Calendar timeout = Calendar.getInstance();
    timeout.setTimeInMillis(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY);
    Duration duration = Duration.ofHours(toHours(new Date(), timeout.getTime()));
    cacheService.set(getAuthRefreshCacheKey(subject), token, duration);
  }

  /**
   * <p>Save the user's pre-verification otp.</p>
   * <br/>
   *
   * @param username the user's identifier to associate with the pre-verification otp or code
   * @param otp a random code associated with the user's identifier during the pre-verification process
   */
  private void savePreVerificationOtp(String username, String otp) {
    cacheService.set(getPreVerificationCacheKey(username), otp, Duration.ofMinutes(3));
  }

  /**
   * <p>Remove the user's pre-verification otp after successful authentication.</p>
   * <br/>
   *
   * @param username the user's identifier associated with the pre-verification otp or code
   */
  private void clearPreVerificationOtp(String username) {
    cacheService.delete(getPreVerificationCacheKey(username));
  }

  /**
   * <p>Save the user's pre-authentication otp.</p>
   * <br/>
   *
   * @param username the user's identifier to associate with the pre-authentication top or code
   * @param otp a random code associated with the user's identifier during the pre-authentication process
   */
  private void savePreAuthenticationOtp(String username, String otp) {
    cacheService.set(getPreAuthenticationCacheKey(username), otp, Duration.ofMinutes(3));
  }

  /**
   * <p>Remove the user's pre-authentication otp after successful authentication.</p>
   * <br/>
   *
   * @param username the user's identifier associated with the pre-verification otp or code
   */
  private void clearPreAuthenticationOtp(String username) {
    cacheService.delete(getPreAuthenticationCacheKey(username));
  }

  /**
   * <p>Save the user's forgot password otp.</p>
   * <br/>
   *
   * @param username the user's identifier to associate with the forgot password otp or code
   * @param otp a random code associated with the user's identifier during the forgot password process
   */
  private void saveResetPasswordOtp(String username, String otp) {
    cacheService.set(getResetPasswordCacheKey(username), otp, Duration.ofMinutes(3));
  }

  /**
   * <p>Save the user's mfa setup otp.</p>
   * <br/>
   *
   * @param username the user's identifier to associate with the mfa setup otp or code
   * @param otp a random code associated with the user's identifier during the mfa setup process
   */
  private void saveMfaSetupOtp(String username, String otp) {
    cacheService.set(getMfaSetupCacheKey(username), otp, Duration.ofMinutes(3));
  }

  /**
   * <p>Remove the user's pre-authentication otp after successful authentication.</p>
   * <br/>
   *
   * @param username the user's identifier associated with the pre-verification otp or code
   */
  private void clearResetPasswordOtp(String username) {
    cacheService.delete(getResetPasswordCacheKey(username));
  }

  /**
   * <p>Remove the user's mfa otp after successful setup and configuration.</p>
   * <br/>
   *
   * @param username the user's identifier associated with the mfa setup otp or code
   */
  private void clearMfaSetupOtp(String username) {
    cacheService.delete(getMfaSetupCacheKey(username));
  }

  /**
   * <p>Prefix a user's identifier with a predefined key used to save an authentication token like JWT.</p>
   * <br/>
   *
   * @param username a user identifier found on the system or is to be registered on the system
   * @return a string concatenation of a predefined prefix and the user's identifier
   */
  public static String getAuthCacheKey(String username) {
    return AUTH_CACHE_PREFIX.concat(username);
  }

  /**
   * <p>Prefix a user's identifier with a predefined key used to save an authentication refresh token like JWT.</p>
   * <br/>
   *
   * @param username a user identifier found on the system or is to be registered on the system
   * @return a string concatenation of a predefined prefix and the user's identifier
   */
  public static String getAuthRefreshCacheKey(String username) {
    return AUTH_REFRESH_CACHE_PREFIX.concat(username);
  }

  /**
   * <p>Prefix a user's identifier with a predefined key used to save a pre-verification token or OTP or code.</p>
   * <br/>
   *
   * @param username a user identifier found on the system or is to be registered on the system
   * @return a string concatenation of a predefined prefix and the user's identifier
   */
  private String getPreVerificationCacheKey(String username) {
    return PRE_VERIFICATION_PREFIX.concat(username);
  }

  /**
   * <p>Prefix a user's identifier with a predefined key used to save a pre-authentication token or OTP or code.</p>
   * <br/>
   *
   * @param username a user identifier found on the system or is to be registered on the system
   * @return a string concatenation of a predefined prefix and the user's identifier
   */
  private String getPreAuthenticationCacheKey(String username) {
    return PRE_AUTHENTICATION_PREFIX.concat(username);
  }

  /**
   * <p>Prefix a user's identifier with a predefined key used to save a reset password token or OTP or code.</p>
   * <br/>
   *
   * @param username a user identifier found on the system or is to be registered on the system
   * @return a string concatenation of a predefined prefix and the user's identifier
   */
  private String getResetPasswordCacheKey(String username) {
    return RESET_PASSWORD_CACHE_PREFIX.concat(username);
  }

  /**
   * <p>Prefix a user's identifier with a predefined key used to save a MFA setup token or OTP or code.</p>
   * <br/>
   *
   * @param username a user identifier found on the system or is to be registered on the system
   * @return a string concatenation of a predefined prefix and the user's identifier
   */
  private String getMfaSetupCacheKey(String username) {
    return MFA_SETUP_CACHE_PREFIX.concat(username);
  }

  /**
   * <p>A way for the user to be able to recover their lost or forgotten credentials so that they can regain access to the system</p>
   * @param dto contains details including email address or username of the user whose account is being recovered
   */
  @Override
  @Transactional
  public void forgotPassword(ForgotPasswordDto dto) {
    Member member = memberService.getMemberByEmailAddress(dto.getEmailAddress());
    if (Objects.isNull(member)) {
      throw new UserNotFoundException(dto.getEmailAddress());
    }

    VerificationType verificationType = VerificationType.valueOf(dto.getVerificationType());
    String code = getRandomSixDigitOtp();

    ProfileToken profileToken;
    Optional<ProfileToken> profileTokenExists = profileTokenService.findByEmailAddress(dto.getEmailAddress());
    if (profileTokenExists.isEmpty()) {
      profileToken = new ProfileToken();
      profileToken.setMember(member);
    } else {
      profileToken = profileTokenExists.get();
    }

    profileToken.setResetPasswordToken(code);
    profileToken.setResetPasswordTokenExpiryDate(addMinutesFromNow(10));
    profileToken.setCreatedOn(LocalDateTime.now());
    profileToken.setUpdatedOn(LocalDateTime.now());
    profileTokenService.save(profileToken);

    FleenUser user = FleenUser.fromMemberBasic(member);
    PreVerificationOrAuthenticationRequest request = createForgotPasswordRequest(code, user);

    sendVerificationMessage(request, verificationType);
    saveResetPasswordOtp(member.getEmailAddress(), code);
  }

  /**
   * <p>Validate the OTP or code sent to the user's email address before allowing them to change their password</p>
   * <br/>
   *
   * @param dto contains details including the identity of user whose account is being recovered and the code to validate the process
   * @return {@link InitiatePasswordChangeResponse} contains a token that allows a user to call change their password
   */
  @Override
  @Transactional(readOnly = true)
  public InitiatePasswordChangeResponse validateResetPasswordCode(ResetPasswordDto dto) {
    Member member = memberService.getMemberByEmailAddress(dto.getEmailAddress());
    if (Objects.isNull(member)) {
      throw new UserNotFoundException(dto.getEmailAddress());
    }

    Optional<ProfileToken> profileTokenExists = profileTokenService.findByEmailAddress(dto.getEmailAddress());
    if (profileTokenExists.isEmpty()) {
      throw new ResetPasswordCodeInvalidException();
    }

    ProfileToken profileToken = profileTokenExists.get();
    if (Objects.isNull(profileToken.getResetPasswordToken())) {
      throw new ResetPasswordCodeInvalidException();
    }

    if (!(profileToken.getResetPasswordToken().equals(dto.getCode()))) {
      throw new ResetPasswordCodeInvalidException();
    }

    if (profileToken.getResetPasswordTokenExpiryDate().isBefore(LocalDateTime.now())) {
      throw new ResetPasswordCodeExpiredException();
    }

    FleenUser user = initAuthentication(member);
    String token = jwtProvider.generateResetPasswordToken(user);

    clearResetPasswordOtp(user.getUsername());
    saveToken(user.getUsername(), token);
    return InitiatePasswordChangeResponse.builder()
            .accessToken(token)
            .build();
  }

  @Override
  @Transactional
  public void changePassword(String username, ChangePasswordDto dto) {
    Member member = memberService.getMemberByEmailAddress(username);
    if (Objects.isNull(member)) {
      throw new UserNotFoundException(username);
    }

    Optional<ProfileToken> profileTokenExists = profileTokenService.findByEmailAddress(username);
    if (profileTokenExists.isPresent() && nonNull(profileTokenExists.get().getResetPasswordToken())) {
      ProfileToken profileToken = profileTokenExists.get();
      profileToken.setResetPasswordToken(null);
      profileToken.setResetPasswordTokenExpiryDate(null);
      profileTokenService.save(profileToken);
    }

    member.setPassword(createEncodedPassword(dto.getPassword()));
    memberService.save(member);
  }

  @Override
  @Transactional
  public SignInResponse completeOnboarding(String username, ChangePasswordDto dto) {
    Member member = memberService.getMemberByEmailAddress(username);
    if (Objects.isNull(member)) {
      throw new UserNotFoundException(username);
    }

    CompleteUserSignUpRequest request = createCompleteUserSignUpRequest(member);
    Set<Role> roles = new HashSet<>();
    roles.add(request.getRole());
    member.setRoles(roles);
    member.setPassword(createEncodedPassword(dto.getPassword()));
    member.setEmailAddressVerified(true);
    MemberStatus memberStatus = memberStatusService.getMemberStatusByCode(MemberStatusType.ACTIVE.name());
    member.setMemberStatus(memberStatus);
    memberService.save(member);

    FleenUser user = FleenUser.fromMember(member);
    Authentication authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    String accessToken = createAccessToken(user);
    String refreshToken = createRefreshToken(user);

    SaveProfileVerificationMessageRequest verificationMessageRequest = SaveProfileVerificationMessageRequest.builder()
            .member(member)
            .emailAddress(member.getEmailAddress())
            .build();

    ProfileVerificationMessage verificationMessage = request.getVerificationMessage();
    if (nonNull(verificationMessage)) {
      sendProfilePreVerificationMessage(member.getEmailAddress(), verificationMessage);

      if (member.getUserType() == ProfileType.USER) {
        verificationMessageRequest.setVerificationStatus(request.getProfileVerificationStatus());
        saveProfileVerificationHistory(request.getVerificationMessage(), verificationMessageRequest);

        ProfileVerificationMessage approvedVerificationMessage = profileVerificationMessageService.getProfileVerificationMessageByType(ProfileVerificationMessageType.APPROVED);
        sendProfilePreVerificationMessage(member.getEmailAddress(), approvedVerificationMessage);
      }
    }

    setContext(authenticationToken);
    saveToken(user.getUsername(), accessToken);
    saveRefreshToken(user.getUsername(), refreshToken);

    SignInResponse signInResponse = SignInResponse
            .builder()
            .emailAddress(member.getEmailAddress())
            .authenticationStatus(COMPLETED)
            .nextAuthentication(NextAuthentication.NONE)
            .mfaEnabled(false)
            .build();

    signInResponse.setAccessToken(accessToken);
    signInResponse.setRefreshToken(refreshToken);
    return signInResponse;
  }

  /**
   * It verifies the validity of the reCaptcha details
   *
   * @param reCaptchaToken the token to check and validate in the ReCaptcha service
   * @return the results of the token verification
   */
  @Override
  public ReCaptchaResponse verifyReCaptcha(String reCaptchaToken) {
    return reCaptchaAdapter.verifyRecaptcha(reCaptchaToken);
  }

  /**
   * <p>When a new user register, it is assigned a role by default that allows them to access basic
   * functionalities of the application.</p>
   * <br/>
   *
   * @param profileType the type of user
   * @return {@link Role} a user role with an authority or authorities
   */
  private Role setNewUserRole(ProfileType profileType) {
    Role role = null;
    switch (profileType) {
      case USER:
        role = roleService.getRoleByCode(PRE_VERIFIED_USER.name());
        break;

      case PROFESSIONAL:
        role = roleService.getRoleByCode(PRE_VERIFIED_PROFESSIONAL.name());
        break;

      case BUSINESS:
        role = roleService.getRoleByCode(PRE_VERIFIED_BUSINESS.name());
        break;
    }
    return role;
  }

  private PreVerificationOrAuthenticationRequest createPreVerificationRequest(String code, FleenUser user) {
    CommonEmailMessageTemplateDetails templateDetails = CommonEmailMessageTemplateDetails.PRE_VERIFICATION;
    PreVerificationOrAuthenticationRequest request = createVerificationRequest(code, user);
    request.setEmailMessageTitle(templateDetails.getEmailMessageSubject());
    request.setSmsMessage(getVerificationSmsMessage(VerificationMessageType.PRE_VERIFICATION));

    String emailBody = getVerificationEmailBody(templateDetails.getTemplateName(), request);
    request.setEmailMessageBody(emailBody);
    return request;
  }

  private PreVerificationOrAuthenticationRequest createPreAuthenticationRequest(String code, FleenUser user) {
    CommonEmailMessageTemplateDetails templateDetails = CommonEmailMessageTemplateDetails.PRE_AUTHENTICATION;
    PreVerificationOrAuthenticationRequest request = createVerificationRequest(code, user);
    request.setEmailMessageTitle(templateDetails.getEmailMessageSubject());
    request.setSmsMessage(getVerificationSmsMessage(VerificationMessageType.PRE_AUTHENTICATION));

    String emailBody = getVerificationEmailBody(templateDetails.getTemplateName(), request);
    request.setEmailMessageBody(emailBody);
    return request;
  }

  private PreVerificationOrAuthenticationRequest createForgotPasswordRequest(String code, FleenUser user) {
    CommonEmailMessageTemplateDetails templateDetails = CommonEmailMessageTemplateDetails.FORGOT_PASSWORD;
    PreVerificationOrAuthenticationRequest request = createVerificationRequest(code, user);
    request.setEmailMessageTitle(templateDetails.getEmailMessageSubject());
    request.setSmsMessage(getVerificationSmsMessage(VerificationMessageType.FORGOT_PASSWORD));

    String emailBody = getVerificationEmailBody(templateDetails.getTemplateName(), request);
    request.setEmailMessageBody(emailBody);
    return request;
  }

  private PreVerificationOrAuthenticationRequest createMfaSetupRequest(String code, FleenUser user) {
    CommonEmailMessageTemplateDetails templateDetails = CommonEmailMessageTemplateDetails.MFA_SETUP;
    PreVerificationOrAuthenticationRequest request = createVerificationRequest(code, user);
    request.setEmailMessageTitle(templateDetails.getEmailMessageSubject());
    request.setSmsMessage(getVerificationSmsMessage(VerificationMessageType.MFA_SETUP));

    String emailBody = getVerificationEmailBody(templateDetails.getTemplateName(), request);
    request.setEmailMessageBody(emailBody);
    return request;
  }

  /**
   * <p>When a user completes a sign up process, the profile of the user is initialized with some basic details</p>
   * <br/>
   *
   * @param member the details of the user that is about to complete a sign-up process
   * @return {@link CompleteUserSignUpRequest} details including the member about to complete the sign-up process and its basic details
   */
  private CompleteUserSignUpRequest createCompleteUserSignUpRequest(Member member) {
    CompleteUserSignUpRequest request = CompleteUserSignUpRequest.builder().build();
    switch (member.getUserType()) {
      case USER:
        request.setRole(roleService.getRoleByCode(USER.name()));
        request.setProfileVerificationStatus(ProfileVerificationStatus.APPROVED);
        member.setVerificationStatus(request.getProfileVerificationStatus());
        break;

      case PROFESSIONAL:
        request.setRole(roleService.getRoleByCode(PRE_APPROVED_PROFESSIONAL.name()));
        break;

      case BUSINESS:
        request.setRole(roleService.getRoleByCode(PRE_APPROVED_BUSINESS.name()));
        break;
    }
    request.setVerificationMessage(profileVerificationMessageService.getProfileVerificationMessageByType(ProfileVerificationMessageType.SIGNUP_COMPLETE));
    return request;
  }

  @Override
  @Transactional
  public void sendMfaVerification(Member member, VerificationType verificationType) {
    String code = getRandomSixDigitOtp();
    FleenUser user = FleenUser.fromMemberBasic(member);
    PreVerificationOrAuthenticationRequest request = createMfaSetupRequest(code, user);

    sendVerificationMessage(request, verificationType);
    saveMfaSetupOtp(member.getEmailAddress(), code);
  }

  @Override
  public void validateMfaSetupCode(String username, String code) {
    String verificationKey = getMfaSetupCacheKey(username);
    validateSmsAndEmailVerificationCode(verificationKey, code);
    clearMfaSetupOtp(username);
  }

  private void initSignInDetails(FleenUser user, SignInResponse response) {
    Authentication authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    String accessToken = createAccessToken(user);
    String refreshToken = createRefreshToken(user);

    setContext(authenticationToken);
    saveToken(user.getUsername(), accessToken);
    saveRefreshToken(user.getUsername(), refreshToken);

    response.setAccessToken(accessToken);
    response.setRefreshToken(refreshToken);
    response.setPhoneNumber(user.getPhoneNumber());
  }

  @Override
  public String createPassword(String rawPassword) {
    return getPasswordEncoder().encode(rawPassword);
  }

  private String generateOtp() {
    return mfaService.generateVerificationOtp(6);
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
    return profileVerificationMessageService;
  }

  @Override
  public VerificationHistoryService getVerificationHistoryService() {
    return verificationHistoryService;
  }

  @Override
  public PasswordEncoder getPasswordEncoder() {
    return passwordEncoder;
  }

  public MemberService getMemberService() {
    return memberService;
  }

  @Override
  public MemberStatusService getMemberStatusService() {
    return memberStatusService;
  }
}
