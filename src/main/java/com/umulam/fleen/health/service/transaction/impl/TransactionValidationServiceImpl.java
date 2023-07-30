package com.umulam.fleen.health.service.transaction.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umulam.fleen.health.constant.paystack.PaystackWebhookEventType;
import com.umulam.fleen.health.constant.session.HealthSessionStatus;
import com.umulam.fleen.health.constant.session.TransactionStatus;
import com.umulam.fleen.health.constant.session.WithdrawalStatus;
import com.umulam.fleen.health.event.CreateSessionMeetingEvent;
import com.umulam.fleen.health.model.domain.HealthSession;
import com.umulam.fleen.health.model.domain.transaction.SessionTransaction;
import com.umulam.fleen.health.model.domain.transaction.WithdrawalTransaction;
import com.umulam.fleen.health.model.event.paystack.PsChargeEvent;
import com.umulam.fleen.health.model.event.paystack.PsTransferEvent;
import com.umulam.fleen.health.model.event.paystack.base.PaystackWebhookEvent;
import com.umulam.fleen.health.repository.jpa.HealthSessionJpaRepository;
import com.umulam.fleen.health.repository.jpa.transaction.SessionTransactionJpaRepository;
import com.umulam.fleen.health.repository.jpa.transaction.WithdrawalTransactionJpaRepository;
import com.umulam.fleen.health.service.impl.FleenHealthEventService;
import com.umulam.fleen.health.service.transaction.TransactionValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.umulam.fleen.health.constant.session.TransactionStatus.FAILED;
import static com.umulam.fleen.health.constant.session.TransactionStatus.SUCCESS;
import static com.umulam.fleen.health.service.session.impl.HealthSessionServiceImpl.getMaxMeetingSessionHourDuration;
import static com.umulam.fleen.health.util.StringUtil.getFullName;

@Slf4j
@Service
public class TransactionValidationServiceImpl implements TransactionValidationService {

  private final HealthSessionJpaRepository healthSessionRepository;
  private final SessionTransactionJpaRepository sessionTransactionJpaRepository;
  private final WithdrawalTransactionJpaRepository withdrawalTransactionJpaRepository;
  private final FleenHealthEventService eventService;
  private final ObjectMapper mapper;

  public TransactionValidationServiceImpl(
    HealthSessionJpaRepository healthSessionRepository,
    SessionTransactionJpaRepository sessionTransactionJpaRepository,
    WithdrawalTransactionJpaRepository withdrawalTransactionJpaRepository,
    FleenHealthEventService eventService,
    ObjectMapper mapper) {
    this.healthSessionRepository = healthSessionRepository;
    this.sessionTransactionJpaRepository = sessionTransactionJpaRepository;
    this.withdrawalTransactionJpaRepository = withdrawalTransactionJpaRepository;
    this.eventService = eventService;
    this.mapper = mapper;
  }

  @Override
  @Transactional
  public void validateAndCompleteTransaction(String body) {
    try {
      PaystackWebhookEvent event = mapper.readValue(body, PaystackWebhookEvent.class);
      if (Objects.equals(event.getEvent(), PaystackWebhookEventType.CHARGE_SUCCESS.getValue())) {
        validateAndCompleteSessionTransaction(body);
      } else if (Objects.equals(event.getEvent(), PaystackWebhookEventType.TRANSFER_SUCCESS.getValue()) ||
          Objects.equals(event.getEvent(), PaystackWebhookEventType.TRANSFER_FAILED.getValue()) ||
          Objects.equals(event.getEvent(), PaystackWebhookEventType.TRANSFER_REVERSED.getValue())) {
        validateAndCompleteWithdrawalTransaction(body);
      }
    } catch (JsonProcessingException ex) {
      log.error(ex.getMessage(), ex);
    }
  }

  private void validateAndCompleteSessionTransaction(String body) {
    try {
      PsChargeEvent event = mapper.readValue(body, PsChargeEvent.class);
      Optional<SessionTransaction> transactionExist = sessionTransactionJpaRepository.findByGroupReference(event.getData().getMetadata().getTransactionReference());
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

              if (healthSession.getStatus() != HealthSessionStatus.SCHEDULED && healthSession.getStatus() != HealthSessionStatus.RESCHEDULED) {
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
                  .timezone(healthSession.getTimezone())
                  .sessionReference(healthSession.getReference())
                  .patientName(patientName)
                  .professionalName(professionalName)
                  .build();

                CreateSessionMeetingEvent.CreateSessionMeetingEventMetadata eventMetadata = CreateSessionMeetingEvent.CreateSessionMeetingEventMetadata.builder()
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

  private void validateAndCompleteWithdrawalTransaction(String body) {
    try {
      PsTransferEvent event = mapper.readValue(body, PsTransferEvent.class);
      Optional<WithdrawalTransaction> transactionExist = withdrawalTransactionJpaRepository.findByReference(event.getData().getReference());
      if (transactionExist.isPresent()) {
        WithdrawalTransaction transaction = transactionExist.get();
        transaction.setExternalSystemReference(event.getData().getTransferCode());
        transaction.setCurrency(event.getData().getCurrency().toUpperCase());
        transaction.setBankName(event.getData().getRecipient().getDetails().getBankName());
        transaction.setAccountNumber(event.getData().getRecipient().getDetails().getAccountNumber());
        transaction.setAccountName(event.getData().getRecipient().getDetails().getAccountName());
        transaction.setBankCode(event.getData().getRecipient().getDetails().getBankCode());
        transaction.setCurrency(event.getData().getCurrency());

        if (SUCCESS.getValue().equalsIgnoreCase(event.getData().getStatus())) {
          if (transaction.getStatus() != SUCCESS) {
            transaction.setStatus(SUCCESS);
            transaction.setWithdrawalStatus(WithdrawalStatus.SUCCESSFUL);
          }
        } else {
          if (FAILED.getValue().equalsIgnoreCase(event.getData().getStatus())) {
            transaction.setStatus(TransactionStatus.FAILED);
            transaction.setWithdrawalStatus(WithdrawalStatus.FAILED);
          } else {
            transaction.setStatus(TransactionStatus.REVERSED);
            transaction.setWithdrawalStatus(WithdrawalStatus.REVERSED);
          }
        }
        withdrawalTransactionJpaRepository.save(transaction);
      }
    } catch (JsonProcessingException ex) {
      log.error(ex.getMessage(), ex);
    }
  }

  private Map<String, String> getCreateSessionMeetingEventMetadata(CreateSessionMeetingEvent.CreateSessionMeetingEventMetadata metadata) {
    return mapper.convertValue(metadata, new TypeReference<>() {});
  }
}
