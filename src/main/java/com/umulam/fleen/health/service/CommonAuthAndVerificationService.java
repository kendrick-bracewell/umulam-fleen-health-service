package com.umulam.fleen.health.service;

import com.umulam.fleen.health.constant.EmailMessageSource;
import com.umulam.fleen.health.constant.MemberStatusType;
import com.umulam.fleen.health.constant.VerificationMessageType;
import com.umulam.fleen.health.constant.authentication.VerificationType;
import com.umulam.fleen.health.constant.base.ProfileType;
import com.umulam.fleen.health.constant.verification.ProfileVerificationMessageType;
import com.umulam.fleen.health.constant.verification.ProfileVerificationStatus;
import com.umulam.fleen.health.exception.authentication.ExpiredVerificationCodeException;
import com.umulam.fleen.health.exception.authentication.InvalidVerificationCodeException;
import com.umulam.fleen.health.exception.authentication.VerificationFailedException;
import com.umulam.fleen.health.model.domain.*;
import com.umulam.fleen.health.model.dto.authentication.VerificationCodeDto;
import com.umulam.fleen.health.model.dto.mail.EmailDetails;
import com.umulam.fleen.health.model.json.SmsMessage;
import com.umulam.fleen.health.model.request.PreVerificationOrAuthenticationRequest;
import com.umulam.fleen.health.model.request.SaveProfileVerificationMessageRequest;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.service.external.aws.EmailServiceImpl;
import com.umulam.fleen.health.service.external.aws.MobileTextService;
import com.umulam.fleen.health.service.impl.CacheService;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.VERIFICATION_CODE_KEY;
import static java.util.Objects.nonNull;

public interface CommonAuthAndVerificationService {


  /**
   * <p>An email message interpolated with data or details for pre-verification purpose sent to user's email address.</p>
   * <br/>
   *
   * @param request contains data like otp to bind with the email template
   * @return an email message that has been prepared from a template and interpolated with all the data required in the message
   */
  default String getVerificationEmailBody(String templateName, PreVerificationOrAuthenticationRequest request) {
    String emailBody = getVerificationTemplate(templateName, Map.of(VERIFICATION_CODE_KEY, request.getCode()));
    if (emailBody == null) {
      throw new VerificationFailedException(request.getErrorMessage());
    }
    return emailBody;
  }

  default SmsMessage getVerificationSmsMessage(VerificationMessageType messageType) {
    Optional<SmsMessage> message = getMobileTextService().getVerificationSmsMessage(messageType);
    if (message.isPresent()) {
      return message.get();
    }
    throw new VerificationFailedException();
  }

  default PreVerificationOrAuthenticationRequest createVerificationRequest(String code, FleenUser user) {
    return PreVerificationOrAuthenticationRequest.builder()
            .code(code)
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .phoneNumber(user.getPhoneNumber())
            .emailAddress(user.getEmailAddress())
            .build();
  }

  /**
   * <p>An email message to be interpolated with data or details for pre-verification purpose sent to user's email address.</p>
   * <br/>
   *
   * @param data contains data like otp to bind with the email template
   * @return an email message that has been prepared from a template and interpolated with all the data required in the message
   */
  default String getVerificationTemplate(String templateName, Map<String, Object> data) {
    return getEmailService().processAndReturnTemplate(templateName, data);
  }

  /**
   * <p>When a user is registering or signing up on the platform, the user is required to complete a verification in which an OTP is sent
   * to the email address or phone number they're planning to use on the platform to confirm their ownership. The OTP will be sent to either of
   * two channels depending on the system or user preference.</p>
   * <br/>
   *
   * @param request contains details like the email address or phone number to send the OTP code to and the code itself
   *            to send to the email address or phone number
   * @param verificationType contains the type of verification as decided by the system or user and where to send the OTP or code to
   */
  default void sendVerificationMessage(PreVerificationOrAuthenticationRequest request, VerificationType verificationType) {
    if (Objects.requireNonNull(verificationType) == VerificationType.SMS) {
      sendSmsPreVerificationOrPreAuthenticationCode(request);
    } else {
      sendEmailPreVerificationOrPreAuthenticationCode(request);
    }
  }

  /**
   * <p>When a user has not completed the registration or sign-up process, an OTP or random code of a fixed length as defined
   * by the system will be sent to the user's email address which will be use to complete the verification found at
   * {@link AuthenticationService#completeSignUp(VerificationCodeDto, FleenUser) completeSignUp} and there exists a similar method but only delivers
   * an SMS message of the OTP to a specified phone number found at {@link #sendSmsPreVerificationOrPreAuthenticationCode(PreVerificationOrAuthenticationRequest) sendSmsPreVerificationOrPreAuthenticationCode}</p>
   * <br/>
   *
   * @param request contains details like the email address to send the OTP code to and the code itself to send to the email address
   */
  default void sendEmailPreVerificationOrPreAuthenticationCode(PreVerificationOrAuthenticationRequest request) {
    EmailDetails emailDetails = EmailDetails.builder()
            .from(EmailMessageSource.BASE.getValue())
            .to(request.getEmailAddress())
            .subject(request.getEmailMessageTitle())
            .htmlText(request.getEmailMessageBody())
            .build();
    getEmailService().sendHtmlMessage(emailDetails);
  }

  /**
   * <p>When a user has not completed the registration or sign-up process, an OTP or random code of a fixed length as defined
   * by the system will be sent to the user's phone number which will be use to complete the verification found at
   * {@link AuthenticationService#completeSignUp(VerificationCodeDto, FleenUser) completeSignUp} and there exists a similar method but only delivers
   * an email message of the OTP to a specified email address found at {@link #sendEmailPreVerificationOrPreAuthenticationCode(PreVerificationOrAuthenticationRequest) sendEmailPreVerificationOrPreAuthenticationCode}</p>
   * <br/>
   *
   * @param request contains details like the phone number to send the OTP code to and the code itself to send to the phone number
   */
  default void sendSmsPreVerificationOrPreAuthenticationCode(PreVerificationOrAuthenticationRequest request) {
    String verificationMessage = MessageFormat.format(request.getSmsMessage().getBody(), request.getCode());
    getMobileTextService().sendSms(request.getPhoneNumber(), verificationMessage);
  }

  /**
   * <p>Validate a code like OTP by checking if it exists in the DB or cache for example {@link CacheService} and confirm if it is equal to the code saved in
   * the store.</p>
   * <br/>
   *
   * @param verificationKey the key to check for the existence of the verification code and the validity of the code associated with it
   * @param code the code to validate against the code saved and associated with the verification key
   */
  default void validateSmsAndEmailVerificationCode(String verificationKey, String code) {
    if (!getCacheService().exists(verificationKey)) {
      throw new ExpiredVerificationCodeException(code);
    }

    if (!getCacheService().get(verificationKey).equals(code)) {
      throw new InvalidVerificationCodeException(code);
    }
  }

  default void initNewUserDetails(Member member, List<Role> newRoles) {
    Set<Role> roles = new HashSet<>(newRoles);
    member.setRoles(roles);

    ProfileVerificationStatus verificationStatus = ProfileVerificationStatus.PENDING;
    MemberStatus memberStatus = getMemberStatusService().getMemberStatusByCode(MemberStatusType.INACTIVE.name());
    member.setMemberStatus(memberStatus);
    member.setVerificationStatus(verificationStatus);
  }

  @Transactional
  default void saveProfileVerificationHistory(SaveProfileVerificationMessageRequest request) {
    ProfileVerificationMessage verificationMessage = getProfileVerificationMessageService()
            .getProfileVerificationMessageByType(request.getVerificationMessageType());
    saveProfileVerificationHistory(verificationMessage, request);
  }

  @Transactional
  default void saveProfileVerificationHistory(ProfileVerificationMessage verificationMessage,
                                              SaveProfileVerificationMessageRequest request) {
    if (nonNull(verificationMessage)) {
      ProfileVerificationHistory history = new ProfileVerificationHistory();
      history.setProfileVerificationStatus(request.getVerificationStatus());
      history.setMember(request.getMember());
      history.setMessage(verificationMessage.getMessage());
      getVerificationHistoryService().saveVerificationHistory(history);
    }
  }

  @Transactional
  default void completeAndApproveUserSignUp(Member member, SaveProfileVerificationMessageRequest verificationMessageRequest, ProfileVerificationMessage verificationMessage) {
    if (member.getUserType() == ProfileType.USER) {
      verificationMessageRequest.setVerificationStatus(member.getVerificationStatus());
      saveProfileVerificationHistory(verificationMessage, verificationMessageRequest);

      ProfileVerificationMessage approvedVerificationMessage = getProfileVerificationMessageService().getProfileVerificationMessageByType(ProfileVerificationMessageType.APPROVED);
      sendProfileVerificationMessage(member.getEmailAddress(), approvedVerificationMessage);
    }
  }

  default void sendProfileVerificationMessage(String emailAddress, ProfileVerificationMessage verificationMessage) {
    if (nonNull(verificationMessage)) {
      EmailDetails emailDetails = EmailDetails.builder()
              .from(EmailMessageSource.BASE.getValue())
              .to(emailAddress)
              .subject(verificationMessage.getTitle())
              .htmlText(verificationMessage.getHtmlMessage())
              .plainText(verificationMessage.getPlainText())
              .build();
      sendAVerificationEmail(emailDetails);
    }
  }

  /**
   * <p>Send a message to the user's email address that inform them about the current stage, the state of their profile verification or status.</p>
   * <br/>
   *
   * @param details contains the message either in HTML or plain text and the recipient to send the message to
   */
  private void sendAVerificationEmail(EmailDetails details) {
    getEmailService().sendHtmlMessage(details);
  }

  default String getRandomSixDigitOtp() {
    return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
  }

  @Transactional
  default void createProfileVerificationMessageNewPendingRegistration(Member member) {
    ProfileVerificationStatus verificationStatus = ProfileVerificationStatus.PENDING;
      SaveProfileVerificationMessageRequest verificationMessageRequest = SaveProfileVerificationMessageRequest
              .builder()
              .verificationMessageType(ProfileVerificationMessageType.PENDING)
              .verificationStatus(verificationStatus)
              .member(member)
              .emailAddress(member.getEmailAddress())
              .build();
    saveProfileVerificationHistory(verificationMessageRequest);
  }

  MobileTextService getMobileTextService();

  EmailServiceImpl getEmailService();

  CacheService getCacheService();

  ProfileVerificationMessageService getProfileVerificationMessageService();

  VerificationHistoryService getVerificationHistoryService();

  MemberStatusService getMemberStatusService();

}
