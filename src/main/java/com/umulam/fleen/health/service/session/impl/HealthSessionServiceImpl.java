package com.umulam.fleen.health.service.session.impl;

import com.umulam.fleen.health.constant.professional.AvailabilityDayOfTheWeek;
import com.umulam.fleen.health.constant.professional.ProfessionalAvailabilityStatus;
import com.umulam.fleen.health.constant.session.*;
import com.umulam.fleen.health.constant.verification.ProfileVerificationStatus;
import com.umulam.fleen.health.event.CancelSessionMeetingEvent;
import com.umulam.fleen.health.event.RescheduleSessionMeetingEvent;
import com.umulam.fleen.health.exception.healthsession.*;
import com.umulam.fleen.health.exception.professional.ProfessionalNotAvailableForSessionDateException;
import com.umulam.fleen.health.exception.professional.ProfessionalNotAvailableForSessionDayException;
import com.umulam.fleen.health.exception.professional.ProfessionalNotAvailableForSessionException;
import com.umulam.fleen.health.exception.professional.ProfessionalProfileNotApproved;
import com.umulam.fleen.health.model.domain.*;
import com.umulam.fleen.health.model.domain.transaction.SessionTransaction;
import com.umulam.fleen.health.model.dto.healthsession.AddHealthSessionReviewDto;
import com.umulam.fleen.health.model.dto.healthsession.BookHealthSessionDto;
import com.umulam.fleen.health.model.dto.healthsession.ReScheduleHealthSessionDto;
import com.umulam.fleen.health.model.mapper.HealthSessionMapper;
import com.umulam.fleen.health.model.mapper.ProfessionalAvailabilityMapper;
import com.umulam.fleen.health.model.mapper.ProfessionalMapper;
import com.umulam.fleen.health.model.request.search.ProfessionalSearchRequest;
import com.umulam.fleen.health.model.response.healthsession.GetProfessionalBookSessionResponse;
import com.umulam.fleen.health.model.response.healthsession.PendingHealthSessionBookingResponse;
import com.umulam.fleen.health.model.response.healthsession.ProfessionalCheckAvailabilityResponse;
import com.umulam.fleen.health.model.response.member.GetMemberUpdateDetailsResponse;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.ProfessionalAvailabilityView;
import com.umulam.fleen.health.model.view.ProfessionalScheduleHealthSessionView;
import com.umulam.fleen.health.model.view.professional.ProfessionalViewBasic;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import com.umulam.fleen.health.repository.jpa.HealthSessionJpaRepository;
import com.umulam.fleen.health.repository.jpa.HealthSessionProfessionalJpaRepository;
import com.umulam.fleen.health.repository.jpa.HealthSessionReviewJpaRepository;
import com.umulam.fleen.health.repository.jpa.ProfessionalAvailabilityJpaRepository;
import com.umulam.fleen.health.repository.jpa.transaction.TransactionJpaRepository;
import com.umulam.fleen.health.service.ExchangeRateService;
import com.umulam.fleen.health.service.MemberService;
import com.umulam.fleen.health.service.ProfessionalService;
import com.umulam.fleen.health.service.impl.ConfigService;
import com.umulam.fleen.health.service.impl.FleenHealthEventService;
import com.umulam.fleen.health.service.session.HealthSessionService;
import com.umulam.fleen.health.util.UniqueReferenceGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.REFERENCE_PREFIX;
import static com.umulam.fleen.health.constant.base.FleenHealthConstant.TRANSACTION_REFERENCE_PREFIX;
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
  private final ProfessionalAvailabilityJpaRepository professionalAvailabilityJpaRepository;
  private final HealthSessionReviewJpaRepository healthSessionReviewJpaRepository;
  private final FleenHealthEventService eventService;
  private final MemberService memberService;
  private final ExchangeRateService exchangeRateService;
  private final ConfigService configService;

  public HealthSessionServiceImpl(
          HealthSessionProfessionalJpaRepository sessionProfessionalJpaRepository,
          HealthSessionJpaRepository healthSessionRepository,
          ProfessionalService professionalService,
          UniqueReferenceGenerator referenceGenerator,
          TransactionJpaRepository transactionJpaRepository,
          ProfessionalAvailabilityJpaRepository professionalAvailabilityJpaRepository,
          HealthSessionReviewJpaRepository healthSessionReviewJpaRepository,
          FleenHealthEventService eventService,
          MemberService memberService,
          ExchangeRateService exchangeRateService,
          ConfigService configService) {
    this.sessionProfessionalJpaRepository = sessionProfessionalJpaRepository;
    this.healthSessionRepository = healthSessionRepository;
    this.professionalService = professionalService;
    this.referenceGenerator = referenceGenerator;
    this.transactionJpaRepository = transactionJpaRepository;
    this.professionalAvailabilityJpaRepository = professionalAvailabilityJpaRepository;
    this.healthSessionReviewJpaRepository = healthSessionReviewJpaRepository;
    this.eventService = eventService;
    this.memberService = memberService;
    this.exchangeRateService = exchangeRateService;
    this.configService = configService;
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

    List<Professional> professionals = new ArrayList<>(page.getContent());
    Collections.shuffle(professionals);
    List<ProfessionalViewBasic> views = ProfessionalMapper.toProfessionalViewsBasic(professionals);
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
    Professional professional = professionalService.getProfessional(professionalId);
    if (professional.getAvailabilityStatus() == ProfessionalAvailabilityStatus.AVAILABLE) {
      return new ProfessionalCheckAvailabilityResponse(true);
    } else {
      return new ProfessionalCheckAvailabilityResponse(false);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public GetProfessionalBookSessionResponse getProfessionalBookSession(Integer professionalId) {
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
  public PendingHealthSessionBookingResponse bookSession(BookHealthSessionDto dto, FleenUser user) {
    HealthSession healthSession = dto.toHealthSession();
    Optional<HealthSession> bookedSessionExist = healthSessionRepository.findByProfessionalAndDateAndTime(healthSession.getProfessional(), healthSession.getDate(), healthSession.getTime());
    if (bookedSessionExist.isPresent()) {
      HealthSession bookedSession = bookedSessionExist.get();
      Member professional = bookedSession.getProfessional();
      if (bookedSession.getPatient().getId().equals(user.getId()) &&
          (bookedSession.getStatus() == HealthSessionStatus.SCHEDULED ||
           bookedSession.getStatus() == HealthSessionStatus.RESCHEDULED ||
           bookedSession.getStatus() == HealthSessionStatus.PENDING)) {
        throw new PatientProfessionalAlreadyBookSessionException(getFullName(professional.getFirstName(), professional.getLastName()), healthSession.getDate(), healthSession.getTime());
      }

      if (!(bookedSession.getPatient().getId().equals(user.getId()))) {
        throw new HealthSessionDateAlreadyBookedException(getFullName(professional.getFirstName(), professional.getLastName()), healthSession.getDate(), healthSession.getTime());
      }
    }

    Optional<Professional> professionalExist = professionalService.findProfessionalByMember(healthSession.getProfessional());
    if (professionalExist.isPresent() && professionalExist.get().getAvailabilityStatus() == ProfessionalAvailabilityStatus.UNAVAILABLE) {
      Member professional = professionalExist.get().getMember();
      throw new ProfessionalNotAvailableForSessionException(getFullName(professional.getFirstName(), professional.getLastName()));
    }

    if (professionalExist.isPresent()) {
      Member professional = professionalExist.get().getMember();

      if (professional.getVerificationStatus() != ProfileVerificationStatus.APPROVED) {
        throw new ProfessionalProfileNotApproved();
      }

      DayOfWeek dayOfWeek = healthSession.getDate().getDayOfWeek();
      AvailabilityDayOfTheWeek availabilityDayOfTheWeek = AvailabilityDayOfTheWeek.valueOf(dayOfWeek.toString());
      List<ProfessionalAvailability> availabilities = professionalAvailabilityJpaRepository.findByMemberAndDayOfWeek(professional, availabilityDayOfTheWeek);
      if (availabilities.isEmpty()) {
        throw new ProfessionalNotAvailableForSessionDayException(getFullName(professional.getFirstName(), professional.getLastName()), dayOfWeek.toString());
      } else {
        boolean timeAvailableForSession = false;
        LocalTime proposedTimeForSession = healthSession.getTime();

        for (ProfessionalAvailability availability : availabilities) {
          if (availability.isTimeInRange(proposedTimeForSession)) {
            timeAvailableForSession = true;
            break;
          }
        }
        if (!timeAvailableForSession) {
          LocalDateTime proposedSessionDateTime = LocalDateTime.of(healthSession.getDate(), healthSession.getTime());
          throw new ProfessionalNotAvailableForSessionDateException(getFullName(professional.getFirstName(), professional.getLastName()), proposedSessionDateTime);
        }
      }
    }

    healthSession.setReference(generateSessionReference());
    Member member = Member.builder()
        .id(user.getId())
        .build();
    healthSession.setPatient(member);
    HealthSession savedHealthSession = healthSessionRepository.save(healthSession);
    GetMemberUpdateDetailsResponse memberDetail = memberService.getMemberGetUpdateDetailsResponse(user);

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

    LocalDateTime startDate = LocalDateTime.of(savedHealthSession.getDate(), savedHealthSession.getTime());
    LocalDateTime endDate = startDate.plusHours(getMaxMeetingSessionHourDuration());

    Double professionalPrice = professionalService.getProfessionalPrice(healthSession.getProfessional().getId());
    Double actualPriceToPayIn = exchangeRateService.getConvertedHealthSessionPrice(professionalPrice);
    return PendingHealthSessionBookingResponse.builder()
      .startDate(startDate)
      .endDate(endDate)
      .patientFirstName(memberDetail.getFirstName())
      .patientLastName(memberDetail.getLastName())
      .patientEmailAddress(memberDetail.getEmailAddress())
      .timezone(savedHealthSession.getTimezone())
      .sessionReference(savedHealthSession.getReference())
      .transactionReference(transaction.getReference())
      .professionalPrice(professionalPrice)
      .actualPriceToPay(actualPriceToPayIn)
      .professionalPriceCurrency(configService.getPricingCurrency())
      .actualPriceCurrency(configService.getPaymentCurrency())
      .build();
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
      return;
    }
    throw new NoAssociatedHealthSessionException(healthSessionId);
  }

  @Override
  @Transactional
  public void rescheduleSession(ReScheduleHealthSessionDto dto, FleenUser user, Integer healthSessionId) {
    boolean healthSessionExist = healthSessionRepository.existsById(healthSessionId);
    if (!healthSessionExist) {
      throw new HealthSessionNotFoundException(healthSessionId);
    }

    HealthSession newHealthSession = dto.toHealthSession();
    Optional<HealthSession> existingHealthSession = healthSessionRepository.findById(healthSessionId);
    if (existingHealthSession.isPresent()) {
      Member patient = memberService.getMemberById(user.getId());
      Member professional = memberService.getMemberById(newHealthSession.getProfessional().getId());

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
        return;
      }

      healthSession.setDate(newHealthSession.getDate());
      healthSession.setTime(newHealthSession.getTime());
      healthSession.setTimezone(newHealthSession.getTimezone());

      LocalDate meetingDate = healthSession.getDate();
      LocalTime meetingTime = healthSession.getTime();

      LocalDateTime meetingStartDateTime = LocalDateTime.of(meetingDate, meetingTime);
      LocalDateTime meetingEndDateTime = meetingStartDateTime.plusHours(getMaxMeetingSessionHourDuration());

      RescheduleSessionMeetingEvent meetingEvent = RescheduleSessionMeetingEvent.builder()
          .startDate(meetingStartDateTime)
          .endDate(meetingEndDateTime)
          .timezone(newHealthSession.getTimezone())
          .meetingEventId(healthSession.getEventReferenceOrId())
          .build();

      eventService.publishRescheduleSession(meetingEvent);
      healthSessionRepository.save(healthSession);
      return;
    }
    throw new NoAssociatedHealthSessionException(healthSessionId);
  }

  @Override
  @Transactional
  public void addSessionReview(AddHealthSessionReviewDto dto, FleenUser user, Integer healthSessionId) {
    Optional<HealthSession> existingHealthSession = healthSessionRepository.findById(healthSessionId);
    if (existingHealthSession.isPresent()) {
      HealthSession healthSession = existingHealthSession.get();
      if (!(healthSession.getPatient().getId().equals(user.getId()))) {
        throw new HealthSessionInvalidTransactionException();
      }

      HealthSessionReview review = dto.toHealthSessionReview();
      review.setPatient(healthSession.getPatient());
      review.setProfessional(healthSession.getProfessional());
      review.setHealthSession(healthSession);
      healthSessionReviewJpaRepository.save(review);
      return;
    }
    throw new NoAssociatedHealthSessionException(healthSessionId);
  }

  private String generateSessionReference() {
    return REFERENCE_PREFIX.concat(referenceGenerator.generateUniqueReference());
  }

  private String generateTransactionReference() {
    return TRANSACTION_REFERENCE_PREFIX.concat(referenceGenerator.generateUniqueReference());
  }

  public static int getMaxMeetingSessionHourDuration() {
    return 1;
  }

}
