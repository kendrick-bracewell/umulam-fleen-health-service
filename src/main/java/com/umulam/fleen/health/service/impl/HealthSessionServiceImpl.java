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
import com.umulam.fleen.health.model.domain.HealthSession;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.Professional;
import com.umulam.fleen.health.model.domain.ProfessionalAvailability;
import com.umulam.fleen.health.model.domain.transaction.SessionTransaction;
import com.umulam.fleen.health.model.dto.healthsession.BookHealthSessionDto;
import com.umulam.fleen.health.model.event.paystack.ChargeEvent;
import com.umulam.fleen.health.model.event.paystack.base.PaystackWebhookEvent;
import com.umulam.fleen.health.model.mapper.ProfessionalMapper;
import com.umulam.fleen.health.model.request.search.ProfessionalSearchRequest;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.ProfessionalAvailabilityView;
import com.umulam.fleen.health.model.view.search.ProfessionalViewBasic;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import com.umulam.fleen.health.repository.jpa.HealthSessionJpaRepository;
import com.umulam.fleen.health.repository.jpa.HealthSessionProfessionalJpaRepository;
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
          FleenHealthEventService eventService,
          MemberService memberService,
          ObjectMapper mapper) {
    this.sessionProfessionalJpaRepository = sessionProfessionalJpaRepository;
    this.healthSessionRepository = healthSessionRepository;
    this.professionalService = professionalService;
    this.referenceGenerator = referenceGenerator;
    this.transactionJpaRepository = transactionJpaRepository;
    this.sessionTransactionJpaRepository = sessionTransactionJpaRepository;
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
  public void getBookSession(FleenUser user, Integer professionalId) {
    Member member = memberService.getMemberById(user.getId());
    Professional professional = professionalService.getProfessional(professionalId);
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

                String patientEmail = "mrkyoungy@gmail.com"; /*healthSession.getPatient().getEmailAddress();*/
                String professionalEmail = healthSession.getProfessional().getEmailAddress();

                CreateSessionMeetingEvent meetingEvent = CreateSessionMeetingEvent.builder()
                  .startDate(meetingStartDateTime)
                  .endDate(meetingEndDateTime)
                  .attendees(List.of(patientEmail, professionalEmail))
                  .timezone(healthSession.getTimeZone())
                  .sessionReference(healthSession.getReference())
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
  public void cancelSession(Integer sessionId, FleenUser user) {
    Member member = memberService.getMemberById(user.getId());
    Optional<HealthSession> healthSessionExist = healthSessionRepository.findByPatient(member);
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
