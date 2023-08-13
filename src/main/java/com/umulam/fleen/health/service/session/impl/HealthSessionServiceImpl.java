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
import com.umulam.fleen.health.util.FleenHealthReferenceGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
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
import java.util.stream.Collectors;

import static com.umulam.fleen.health.model.dto.healthsession.BookHealthSessionDto.SessionPeriod;
import static com.umulam.fleen.health.model.response.healthsession.PendingHealthSessionBookingResponse.BookedSessionPeriod;
import static com.umulam.fleen.health.util.DateTimeUtil.toDate;
import static com.umulam.fleen.health.util.DateTimeUtil.toTime;
import static com.umulam.fleen.health.util.FleenHealthUtil.areNotEmpty;
import static com.umulam.fleen.health.util.FleenHealthUtil.toSearchResult;
import static com.umulam.fleen.health.util.StringUtil.getFullName;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

@Slf4j
@Service
@Primary
public class HealthSessionServiceImpl implements HealthSessionService {

  protected final HealthSessionProfessionalJpaRepository sessionProfessionalJpaRepository;
  protected final HealthSessionJpaRepository healthSessionRepository;
  protected final ProfessionalService professionalService;
  protected final FleenHealthReferenceGenerator referenceGenerator;
  protected final TransactionJpaRepository transactionJpaRepository;
  protected final ProfessionalAvailabilityJpaRepository professionalAvailabilityJpaRepository;
  protected final HealthSessionReviewJpaRepository healthSessionReviewJpaRepository;
  protected final FleenHealthEventService eventService;
  protected final MemberService memberService;
  protected final ExchangeRateService exchangeRateService;
  protected final ConfigService configService;

  public HealthSessionServiceImpl(
          HealthSessionProfessionalJpaRepository sessionProfessionalJpaRepository,
          HealthSessionJpaRepository healthSessionRepository,
          ProfessionalService professionalService,
          FleenHealthReferenceGenerator referenceGenerator,
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
    for (SessionPeriod period : dto.getPeriods()) {
      Optional<HealthSession> bookedSessionExist = healthSessionRepository.findByProfessionalAndDateAndTime(healthSession.getProfessional(), toDate(period.getDate()), toTime(period.getTime()));
      if (bookedSessionExist.isPresent()) {
        HealthSession bookedSession = bookedSessionExist.get();
        Member professional = bookedSession.getProfessional();
        String professionalName = getFullName(professional.getFirstName(), professional.getLastName());

        if (bookedSession.getStatus() == HealthSessionStatus.SCHEDULED ||
            bookedSession.getStatus() == HealthSessionStatus.RESCHEDULED ||
            bookedSession.getStatus() == HealthSessionStatus.PENDING) {
          if (bookedSession.getPatient().getId().equals(user.getId())) {
            throw new PatientProfessionalAlreadyBookSessionException(professionalName, bookedSession.getDate(), bookedSession.getTime());
          } else  {
            throw new HealthSessionDateAlreadyBookedException(professionalName, bookedSession.getDate(), bookedSession.getTime());
          }
        }
      }
    }


    Optional<Professional> professionalExist = professionalService.findProfessionalByMember(healthSession.getProfessional());
    if (professionalExist.isPresent()) {
      Professional professional = professionalExist.get();
      Member member = professional.getMember();
      String professionalName = getFullName(member.getFirstName(), member.getLastName());

      if (professional.getAvailabilityStatus() == ProfessionalAvailabilityStatus.UNAVAILABLE
          || member.getVerificationStatus() != ProfileVerificationStatus.APPROVED) {
        throw new ProfessionalNotAvailableForSessionException(professionalName);
      }

      List<ProfessionalAvailability> availabilities = professionalAvailabilityJpaRepository.findAllByMember(member);
      if (availabilities.isEmpty()) {
        throw new ProfessionalNotAvailableForSessionException(professionalName);
      }

      for (SessionPeriod sessionPeriod: dto.getPeriods()) {
        LocalDate proposedDateForSession = requireNonNull(toDate(sessionPeriod.getDate()));
        LocalTime proposedTimeForSession = requireNonNull(toTime(sessionPeriod.getTime()));

        DayOfWeek dayOfWeek = proposedDateForSession.getDayOfWeek();
        AvailabilityDayOfTheWeek availabilityDayOfTheWeek = AvailabilityDayOfTheWeek.valueOf(dayOfWeek.toString());
        List<ProfessionalAvailability> proposedDayOfAvailability = availabilities
          .stream()
          .filter(availability -> availability.getDayOfWeek() == availabilityDayOfTheWeek)
          .collect(Collectors.toList());

        if (proposedDayOfAvailability.isEmpty()) {
          throw new ProfessionalNotAvailableForSessionDayException(professionalName, dayOfWeek.toString());
        } else {
          boolean timeAvailableForSession = false;
          for (ProfessionalAvailability availability : proposedDayOfAvailability) {
            if (availability.isTimeInRange(proposedTimeForSession)) {
              timeAvailableForSession = true;
              break;
            }
          }

          if (!timeAvailableForSession) {
            LocalDateTime proposedSessionDateTime = LocalDateTime.of(proposedDateForSession, proposedTimeForSession);
            throw new ProfessionalNotAvailableForSessionDateException(professionalName, proposedSessionDateTime);
          }
        }
      }
    }

    Member patient = user.toMember();
    List<HealthSession> healthSessions = new ArrayList<>();
    for (SessionPeriod period : dto.getPeriods()) {
      HealthSession newHealthSession = HealthSession.builder()
        .documentLink(dto.getDocument())
        .professional(healthSession.getProfessional())
        .patient(patient)
        .location(healthSession.getLocation())
        .timezone(healthSession.getTimezone())
        .comment(healthSession.getComment())
        .date(toDate(period.getDate()))
        .time(toTime(period.getTime()))
        .reference(referenceGenerator.generateSessionReference())
        .status(HealthSessionStatus.PENDING)
        .build();

      healthSessions.add(newHealthSession);
    }

    List<HealthSession> savedHealthSessions = healthSessionRepository.saveAll(healthSessions);
    GetMemberUpdateDetailsResponse memberDetail = memberService.getMemberGetUpdateDetailsResponse(user);

    Double professionalPrice = professionalService.getProfessionalPrice(healthSession.getProfessional().getId());
    int totalNumberOfSessions = dto.getPeriods().size();
    double totalAmountToCharge = professionalPrice * totalNumberOfSessions;
    String groupTransactionReference = referenceGenerator.generateGroupTransactionReference();
    Double actualPriceToPayForSession = exchangeRateService.getConvertedHealthSessionPrice(totalAmountToCharge);

    List<SessionTransaction> transactions = new ArrayList<>();
    for (HealthSession session : savedHealthSessions) {
      SessionTransaction transaction = SessionTransaction.builder()
        .reference(referenceGenerator.generateTransactionReference())
        .sessionReference(session.getReference())
        .groupTransactionReference(groupTransactionReference)
        .payer(patient)
        .amount(professionalPrice)
        .totalSessions(totalNumberOfSessions)
        .status(TransactionStatus.PENDING)
        .gateway(PaymentGateway.FLUTTERWAVE)
        .type(TransactionType.HEALTH_SESSION)
        .subType(TransactionSubType.DEBIT)
        .currency(CurrencyType.NGN.getValue())
        .paymentCurrency(configService.getHealthSessionPaymentCurrency())
        .amountInPaymentCurrency(actualPriceToPayForSession)
        .build();
      transactions.add(transaction);
    }
    transactionJpaRepository.saveAll(transactions);

    List<BookedSessionPeriod> bookedPeriods = new ArrayList<>();
    for (HealthSession session : savedHealthSessions) {
      LocalDateTime startDate = LocalDateTime.of(session.getDate(), session.getTime());
      LocalDateTime endDate = startDate.plusHours(getMaxMeetingSessionHourDuration());

      BookedSessionPeriod sessionPeriod = BookedSessionPeriod.builder()
        .startDate(startDate)
        .endDate(endDate)
        .build();
      bookedPeriods.add(sessionPeriod);
    }

    return PendingHealthSessionBookingResponse.builder()
      .timezone(healthSession.getTimezone())
      .bookedPeriods(bookedPeriods)
      .patientFirstName(memberDetail.getFirstName())
      .patientLastName(memberDetail.getLastName())
      .patientEmailAddress(memberDetail.getEmailAddress())
      .transactionReference(groupTransactionReference)
      .professionalPrice(professionalPrice)
      .actualPriceToPay(actualPriceToPayForSession)
      .professionalPriceCurrency(configService.getHealthSessionPricingCurrency())
      .actualPriceCurrency(configService.getHealthSessionPaymentCurrency())
      .build();
  }

  @Override
  @Transactional
  public void cancelSession(FleenUser user, Integer healthSessionId) {
    Member member = memberService.getMemberById(user.getId());
    Optional<HealthSession> healthSessionExist = healthSessionRepository.findByPatientAndId(member, healthSessionId);
    cancelSession(healthSessionExist, healthSessionId);
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

  @Override
  @Transactional
  public void cancelSession(Optional<HealthSession> healthSessionExist, Integer healthSessionId) {
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

  public static int getMaxMeetingSessionHourDuration() {
    return 1;
  }

}
