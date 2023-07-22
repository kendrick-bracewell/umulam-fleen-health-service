package com.umulam.fleen.health.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umulam.fleen.health.constant.paystack.PaystackWebhookEventType;
import com.umulam.fleen.health.constant.professional.ProfessionalAvailabilityStatus;
import com.umulam.fleen.health.constant.session.*;
import com.umulam.fleen.health.constant.verification.ProfileVerificationStatus;
import com.umulam.fleen.health.event.CancelSessionMeetingEvent;
import com.umulam.fleen.health.event.CreateSessionMeetingEvent;
import com.umulam.fleen.health.event.RescheduleSessionMeetingEvent;
import com.umulam.fleen.health.exception.healthsession.*;
import com.umulam.fleen.health.model.domain.HealthSession;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.Professional;
import com.umulam.fleen.health.model.domain.ProfessionalAvailability;
import com.umulam.fleen.health.model.domain.transaction.SessionTransaction;
import com.umulam.fleen.health.model.dto.healthsession.BookHealthSessionDto;
import com.umulam.fleen.health.model.dto.healthsession.ReScheduleHealthSessionDto;
import com.umulam.fleen.health.model.event.paystack.ChargeEvent;
import com.umulam.fleen.health.model.event.paystack.base.PaystackWebhookEvent;
import com.umulam.fleen.health.model.mapper.HealthSessionMapper;
import com.umulam.fleen.health.model.mapper.ProfessionalAvailabilityMapper;
import com.umulam.fleen.health.model.mapper.ProfessionalMapper;
import com.umulam.fleen.health.model.request.search.ProfessionalSearchRequest;
import com.umulam.fleen.health.model.response.professional.GetProfessionalBookSessionResponse;
import com.umulam.fleen.health.model.response.professional.ProfessionalCheckAvailabilityResponse;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.ProfessionalAvailabilityView;
import com.umulam.fleen.health.model.view.ProfessionalScheduleHealthSessionView;
import com.umulam.fleen.health.model.view.search.ProfessionalViewBasic;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import com.umulam.fleen.health.repository.jpa.HealthSessionJpaRepository;
import com.umulam.fleen.health.repository.jpa.HealthSessionProfessionalJpaRepository;
import com.umulam.fleen.health.repository.jpa.ProfessionalAvailabilityJpaRepository;
import com.umulam.fleen.health.repository.jpa.transaction.SessionTransactionJpaRepository;
import com.umulam.fleen.health.repository.jpa.transaction.TransactionJpaRepository;
import com.umulam.fleen.health.service.HealthSessionService;
import com.umulam.fleen.health.service.MemberService;
import com.umulam.fleen.health.service.ProfessionalService;
import com.umulam.fleen.health.util.UniqueReferenceGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.REFERENCE_PREFIX;
import static com.umulam.fleen.health.constant.base.FleenHealthConstant.TRANSACTION_REFERENCE_PREFIX;
import static com.umulam.fleen.health.constant.session.TransactionStatus.SUCCESS;
import static com.umulam.fleen.health.event.CreateSessionMeetingEvent.CreateSessionMeetingEventMetadata;
import static com.umulam.fleen.health.util.FleenHealthUtil.areNotEmpty;
import static com.umulam.fleen.health.util.FleenHealthUtil.toSearchResult;
import static com.umulam.fleen.health.util.StringUtil.getFullName;
import static java.util.Objects.nonNull;

@Slf4j
@Service
public class HealthSessionServiceImpl implements HealthSessionService {

  private final HealthSessionProfessionalJpaRepository sessionProfessionalJpaRepository;
  private final HealthSessionJpaRepository healthSessionRepository;
  private final ProfessionalService professionalService;
  private final UniqueReferenceGenerator referenceGenerator;
  private final TransactionJpaRepository transactionJpaRepository;
  private final SessionTransactionJpaRepository sessionTransactionJpaRepository;
  private final ProfessionalAvailabilityJpaRepository professionalAvailabilityJpaRepository;
  private final FleenHealthEventService eventService;
  private final MemberService memberService;
  private final ObjectMapper mapper;

  public HealthSessionServiceImpl(
          HealthSessionProfessionalJpaRepository sessionProfessionalJpaRepository,
          HealthSessionJpaRepository healthSessionRepository,
          ProfessionalService professionalService,
          UniqueReferenceGenerator referenceGenerator,
          TransactionJpaRepository transactionJpaRepository,
          SessionTransactionJpaRepository sessionTransactionJpaRepository,
          ProfessionalAvailabilityJpaRepository professionalAvailabilityJpaRepository,
          FleenHealthEventService eventService,
          MemberService memberService,
          ObjectMapper mapper) {
    this.sessionProfessionalJpaRepository = sessionProfessionalJpaRepository;
    this.healthSessionRepository = healthSessionRepository;
    this.professionalService = professionalService;
    this.referenceGenerator = referenceGenerator;
    this.transactionJpaRepository = transactionJpaRepository;
    this.sessionTransactionJpaRepository = sessionTransactionJpaRepository;
    this.professionalAvailabilityJpaRepository = professionalAvailabilityJpaRepository;
    this.eventService = eventService;
    this.memberService = memberService;
    this.mapper = mapper;
  }

  @Override
  @Transactional(readOnly = true)
  public SearchResultView viewProfessionals(ProfessionalSearchRequest req) {
    Page<Professional> page;
    ProfessionalAvailabilityStatus availability = ProfessionalAvailabilityStatus.AVAILABLE;
    ProfileVerificationStatus verificationStatus = ProfileVerificationStatus.APPROVED;

    if (areNotEmpty(req.getFirstName(), req.getLastName())) {
      page = sessionProfessionalJpaRepository.findByFirstNameAndLastName(req.getFirstName(), req.getLastName(), availability, verificationStatus, req.getPage());
    } else if (nonNull(req.getProfessionalType())) {
      page = sessionProfessionalJpaRepository.findByProfessionalType(req.getProfessionalType(), availability, verificationStatus, req.getPage());
    } else if (nonNull(req.getQualificationType())) {
      page = sessionProfessionalJpaRepository.findByQualification(req.getQualificationType(), availability, verificationStatus, req.getPage());
    } else if (nonNull(req.getLanguageSpoken())) {
      page = sessionProfessionalJpaRepository.findByLanguageSpoken(req.getLanguageSpoken(), availability, verificationStatus, req.getPage());
    } else {
      page = sessionProfessionalJpaRepository.findByAvailabilityStatus(availability, verificationStatus, req.getPage());
    }

    Collections.shuffle(page.getContent());
    List<ProfessionalViewBasic> views = ProfessionalMapper.toProfessionalViewsBasic(page.getContent());
    return toSearchResult(views, page);
  }

  @Override
  @Transactional(readOnly = true)
  public ProfessionalViewBasic viewProfessionalDetail(Integer professionalId) {
    return professionalService.findProfessionalBasicById(professionalId);
  }

  @Override
  @Transactional(readOnly = true)
  public ProfessionalCheckAvailabilityResponse viewProfessionalAvailability(FleenUser user, Integer professionalId) {
    memberService.isMemberExistsById(user.getId());
    Professional professional = professionalService.getProfessional(professionalId);
    if (professional.getAvailabilityStatus() == ProfessionalAvailabilityStatus.AVAILABLE) {
      return new ProfessionalCheckAvailabilityResponse(true);
    } else {
      return new ProfessionalCheckAvailabilityResponse(false);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public GetProfessionalBookSessionResponse getProfessionalBookSession(FleenUser user, Integer professionalId) {
    memberService.isMemberExistsById(user.getId());
    Professional professional = professionalService.getProfessional(professionalId);
    List<ProfessionalAvailability> availabilities = professionalAvailabilityJpaRepository.findAllByMember(professional.getMember());
    List<ProfessionalAvailabilityView> availabilityPeriod = ProfessionalAvailabilityMapper.toProfessionalAvailabilityViews(availabilities);
    List<HealthSession> healthSessions = healthSessionRepository.findByProfessionalAndDateAfter(professional.getMember(), LocalDate.now());
    List<ProfessionalScheduleHealthSessionView> scheduledSessions = HealthSessionMapper.toProfessionalScheduledHealthSessionViews(healthSessions);
    return GetProfessionalBookSessionResponse.builder()
      .availabilityPeriods(availabilityPeriod)
      .scheduledSessions(scheduledSessions)
      .build();
  }

  @Override
  @Transactional
  public void bookSession(BookHealthSessionDto dto, FleenUser user) {
    HealthSession healthSession = dto.toHealthSession();
    healthSession.setReference(generateSessionReference());
    Member member = Member.builder()
        .id(user.getId())
        .build();
    healthSession.setPatient(member);
    HealthSession savedHealthSession = healthSessionRepository.save(healthSession);
    SessionTransaction transaction = SessionTransaction.builder()
        .reference(generateTransactionReference())
        .payer(member)
        .status(TransactionStatus.PENDING)
        .gateway(PaymentGateway.PAYSTACK)
        .amount(dto.getTransactionData().getAmount())
        .type(TransactionType.SESSION)
        .subType(TransactionSubType.DEBIT)
        .sessionReference(savedHealthSession.getReference())
        .build();

    transactionJpaRepository.save(transaction);
  }

  @Override
  @Transactional
  public void validateAndCompleteTransaction(String body) {
    try {
      PaystackWebhookEvent event = mapper.readValue(body, PaystackWebhookEvent.class);
      if (Objects.equals(event.getEvent(), PaystackWebhookEventType.CHARGE_SUCCESS.getValue())) {
        validateAndCompleteSessionTransaction(body);
      }
    } catch (JsonProcessingException ex) {
      log.error(ex.getMessage(), ex);
    }
  }

  private void validateAndCompleteSessionTransaction(String body) {
    try {
      ChargeEvent event = mapper.readValue(body, ChargeEvent.class);
      Optional<SessionTransaction> transactionExist = sessionTransactionJpaRepository.findByReference(event.getData().getMetadata().getTransactionReference());
      if (SUCCESS.getValue().equalsIgnoreCase(event.getData().getStatus())) {
        if (transactionExist.isPresent()) {
          SessionTransaction transaction = transactionExist.get();

          if (transaction.getStatus() != SUCCESS) {
            transaction.setStatus(SUCCESS);
            transaction.setExternalSystemReference(event.getData().getReference());
            transaction.setCurrency(event.getData().getCurrency().toUpperCase());

            Optional<HealthSession> healthSessionExist = healthSessionRepository.findByReference(transaction.getSessionReference());
            if (healthSessionExist.isPresent()) {
              HealthSession healthSession = healthSessionExist.get();

              if (healthSession.getStatus() != HealthSessionStatus.APPROVED && healthSession.getStatus() != HealthSessionStatus.RESCHEDULED) {
                LocalDate meetingDate = healthSession.getDate();
                LocalTime meetingTime = healthSession.getTime();

                LocalDateTime meetingStartDateTime = LocalDateTime.of(meetingDate, meetingTime);
                LocalDateTime meetingEndDateTime = meetingStartDateTime.plusHours(getMaxMeetingSessionHourDuration());

                String patientEmail = healthSession.getPatient().getEmailAddress();
                String professionalEmail = healthSession.getProfessional().getEmailAddress();
                String patientName = getFullName(healthSession.getPatient().getFirstName(), healthSession.getPatient().getLastName());
                String professionalName = getFullName(healthSession.getProfessional().getFirstName(), healthSession.getProfessional().getLastName());

                CreateSessionMeetingEvent meetingEvent = CreateSessionMeetingEvent.builder()
                  .startDate(meetingStartDateTime)
                  .endDate(meetingEndDateTime)
                  .attendees(List.of(patientEmail, professionalEmail))
                  .timezone(healthSession.getTimeZone())
                  .sessionReference(healthSession.getReference())
                  .patientName(patientName)
                  .professionalName(professionalName)
                  .build();

                CreateSessionMeetingEventMetadata eventMetadata = CreateSessionMeetingEventMetadata.builder()
                  .sessionReference(healthSession.getReference())
                  .build();
                meetingEvent.setMetadata(getCreateSessionMeetingEventMetadata(eventMetadata));
                eventService.publishCreateSession(meetingEvent);
              }
              sessionTransactionJpaRepository.save(transaction);
            }
          }
        }
      } else {
        if (transactionExist.isPresent()) {
          SessionTransaction transaction = transactionExist.get();
          transaction.setStatus(TransactionStatus.FAILED);
          transaction.setExternalSystemReference(event.getData().getReference());
          transaction.setCurrency(event.getData().getCurrency().toUpperCase());
          sessionTransactionJpaRepository.save(transaction);
        }
      }
    } catch (JsonProcessingException ex) {
      log.error(ex.getMessage(), ex);
    }
  }

  @Override
  @Transactional
  public void cancelSession(FleenUser user, Integer healthSessionId) {
    Member member = memberService.getMemberById(user.getId());
    Optional<HealthSession> healthSessionExist = healthSessionRepository.findByPatientAndId(member, healthSessionId);
    if (healthSessionExist.isPresent()) {
      HealthSession healthSession = healthSessionExist.get();
      healthSession.setStatus(HealthSessionStatus.CANCELED);
      healthSessionRepository.save(healthSession);
      CancelSessionMeetingEvent event = CancelSessionMeetingEvent.builder()
        .eventIdOrReference(healthSession.getEventReferenceOrId())
        .otherEventReference(healthSession.getOtherEventReference())
        .sessionReference(healthSession.getReference())
        .build();
      eventService.publishCancelSession(event);
    }
    throw new NoAssociatedHealthSessionException(healthSessionId);
  }

  @Override
  @Transactional
  public Object rescheduleSession(ReScheduleHealthSessionDto dto, FleenUser user, Integer healthSessionId) {
    boolean healthSessionExist = healthSessionRepository.existsById(healthSessionId);
    if (!healthSessionExist) {
      throw new HealthSessionNotFoundException(healthSessionId);
    }

    Member patient = memberService.getMemberById(user.getId());
    HealthSession newHealthSession = dto.toHealthSession();
    Member professional = memberService.getMemberById(newHealthSession.getProfessional().getId());
    Optional<HealthSession> existingHealthSession = healthSessionRepository.findById(healthSessionId);
    if (existingHealthSession.isPresent()) {
      HealthSession healthSession = existingHealthSession.get();
      if (healthSession.getStatus() == HealthSessionStatus.CANCELED) {
        throw new HealthSessionCanceledAlreadyException();
      }

      if (healthSession.getStatus() == HealthSessionStatus.COMPLETED) {
        throw new HealthSessionAlreadyCompletedException();
      }

      if (healthSession.getStatus() == HealthSessionStatus.PENDING) {
        throw new HealthSessionPaymentNotConfirmedException();
      }

      Optional<HealthSession> bookedSessionExist = healthSessionRepository.findByProfessionalAndDateAndTime(professional, newHealthSession.getDate(), newHealthSession.getTime());
      if (bookedSessionExist.isPresent() && !(bookedSessionExist.get().getPatient().getId().equals(patient.getId()))) {
        throw new HealthSessionDateAlreadyBookedException(getFullName(professional.getFirstName(), professional.getLastName()), newHealthSession.getDate(), newHealthSession.getTime());
      }

      if (healthSession.getDate().equals(newHealthSession.getDate()) && healthSession.getTime().equals(newHealthSession.getTime())) {
        return null;
      }

      healthSession.setDate(newHealthSession.getDate());
      healthSession.setTime(newHealthSession.getTime());
      healthSession.setTimeZone(newHealthSession.getTimeZone());

      LocalDate meetingDate = healthSession.getDate();
      LocalTime meetingTime = healthSession.getTime();

      LocalDateTime meetingStartDateTime = LocalDateTime.of(meetingDate, meetingTime);
      LocalDateTime meetingEndDateTime = meetingStartDateTime.plusHours(getMaxMeetingSessionHourDuration());

      RescheduleSessionMeetingEvent meetingEvent = RescheduleSessionMeetingEvent.builder()
          .startDate(meetingStartDateTime)
          .endDate(meetingEndDateTime)
          .timezone(newHealthSession.getTimeZone())
          .meetingEventId(healthSession.getEventReferenceOrId())
          .build();

      eventService.publishRescheduleSession(meetingEvent);
      healthSessionRepository.save(healthSession);
    }
    throw new NoAssociatedHealthSessionException(healthSessionId);
  }

  private String generateSessionReference() {
    return REFERENCE_PREFIX.concat(referenceGenerator.generateUniqueReference());
  }

  private String generateTransactionReference() {
    return TRANSACTION_REFERENCE_PREFIX.concat(referenceGenerator.generateUniqueReference());
  }

  private int getMaxMeetingSessionHourDuration() {
    return 1;
  }

  private Map<String, String> getCreateSessionMeetingEventMetadata(CreateSessionMeetingEventMetadata metadata) {
    return mapper.convertValue(metadata, new TypeReference<>() {});
  }
}
