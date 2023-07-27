package com.umulam.fleen.health.service.admin.impl;

import com.umulam.fleen.health.constant.base.ProfileType;
import com.umulam.fleen.health.constant.member.MemberGender;
import com.umulam.fleen.health.constant.session.HealthSessionStatus;
import com.umulam.fleen.health.constant.session.TransactionStatus;
import com.umulam.fleen.health.constant.verification.ProfileVerificationStatus;
import com.umulam.fleen.health.model.statistic.GeneralStatistic;
import com.umulam.fleen.health.model.statistic.HealthSessionStatistic;
import com.umulam.fleen.health.model.statistic.MemberStatistic;
import com.umulam.fleen.health.model.statistic.SessionTransactionStatistic;
import com.umulam.fleen.health.repository.jpa.*;
import com.umulam.fleen.health.repository.jpa.admin.statistic.AdminHealthSessionStatisticJpaRepository;
import com.umulam.fleen.health.repository.jpa.admin.statistic.AdminMemberStatisticJpaRepository;
import com.umulam.fleen.health.repository.jpa.admin.statistic.AdminSessionTransactionStatisticJpaRepository;
import com.umulam.fleen.health.service.admin.AdminStatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdminStatisticServiceImpl implements AdminStatisticService {

  private final AdminHealthSessionStatisticJpaRepository adminHealthSessionStatisticJpaRepository;
  private final AdminSessionTransactionStatisticJpaRepository adminSessionTransactionStatisticJpaRepository;
  private final AdminMemberStatisticJpaRepository adminMemberStatisticJpaRepository;
  private final CountryJpaRepository countryJpaRepository;
  private final BusinessJpaRepository businessJpaRepository;
  private final ProfessionalJpaRepository professionalJpaRepository;
  private final RoleJpaRepository roleJpaRepository;
  private final MemberStatusJpaRepository memberStatusJpaRepository;
  private final ProfessionalAvailabilityJpaRepository professionalAvailabilityJpaRepository;
  private final ProfileVerificationMessageJpaRepository profileVerificationMessageJpaRepository;
  private final VerificationHistoryJpaRepository verificationHistoryJpaRepository;
  private final HealthSessionReviewJpaRepository healthSessionReviewJpaRepository;
  private final MemberJpaRepository memberJpaRepository;
  private final VerificationDocumentJpaRepository verificationDocumentJpaRepository;

  public AdminStatisticServiceImpl(AdminHealthSessionStatisticJpaRepository adminHealthSessionStatisticJpaRepository,
                                   AdminSessionTransactionStatisticJpaRepository adminSessionTransactionStatisticJpaRepository,
                                   AdminMemberStatisticJpaRepository adminMemberStatisticJpaRepository,
                                   CountryJpaRepository countryJpaRepository,
                                   BusinessJpaRepository businessJpaRepository,
                                   ProfessionalJpaRepository professionalJpaRepository,
                                   RoleJpaRepository roleJpaRepository,
                                   MemberStatusJpaRepository memberStatusJpaRepository,
                                   ProfessionalAvailabilityJpaRepository professionalAvailabilityJpaRepository,
                                   ProfileVerificationMessageJpaRepository profileVerificationMessageJpaRepository,
                                   VerificationHistoryJpaRepository verificationHistoryJpaRepository,
                                   HealthSessionReviewJpaRepository healthSessionReviewJpaRepository,
                                   MemberJpaRepository memberJpaRepository,
                                   VerificationDocumentJpaRepository verificationDocumentJpaRepository) {
    this.adminHealthSessionStatisticJpaRepository = adminHealthSessionStatisticJpaRepository;
    this.adminSessionTransactionStatisticJpaRepository = adminSessionTransactionStatisticJpaRepository;
    this.adminMemberStatisticJpaRepository = adminMemberStatisticJpaRepository;
    this.countryJpaRepository = countryJpaRepository;
    this.businessJpaRepository = businessJpaRepository;
    this.professionalJpaRepository = professionalJpaRepository;
    this.roleJpaRepository = roleJpaRepository;
    this.memberStatusJpaRepository = memberStatusJpaRepository;
    this.professionalAvailabilityJpaRepository = professionalAvailabilityJpaRepository;
    this.profileVerificationMessageJpaRepository = profileVerificationMessageJpaRepository;
    this.verificationHistoryJpaRepository = verificationHistoryJpaRepository;
    this.healthSessionReviewJpaRepository = healthSessionReviewJpaRepository;
    this.memberJpaRepository = memberJpaRepository;
    this.verificationDocumentJpaRepository = verificationDocumentJpaRepository;
  }

  @Override
  public HealthSessionStatistic getHealthSessionStatistics() {
    long totalNumberOfSessions = adminHealthSessionStatisticJpaRepository.countTotalNumberOfSessions();
    long totalNumberOfPendingSessions = adminHealthSessionStatisticJpaRepository.countTotalNumberOfSessions(HealthSessionStatus.PENDING);
    long totalNumberOfCompletedSessions = adminHealthSessionStatisticJpaRepository.countTotalNumberOfSessions(HealthSessionStatus.COMPLETED);
    long totalNumberOfScheduleSessions = adminHealthSessionStatisticJpaRepository.countTotalNumberOfSessions(HealthSessionStatus.SCHEDULED);
    long totalNumberOfRescheduledSessions = adminHealthSessionStatisticJpaRepository.countTotalNumberOfSessions(HealthSessionStatus.RESCHEDULED);
    return HealthSessionStatistic.builder()
      .totalNumberOfSessions(totalNumberOfSessions)
      .totalNumberOfPendingSessions(totalNumberOfPendingSessions)
      .totalNumberOfCompletedSessions(totalNumberOfCompletedSessions)
      .totalNumberOfScheduleSessions(totalNumberOfScheduleSessions)
      .totalNumberOfRescheduledSessions(totalNumberOfRescheduledSessions)
      .build();
  }

  @Override
  public SessionTransactionStatistic getSessionTransactionStatistics() {
    long totalNumberOfSessionTransactions = adminSessionTransactionStatisticJpaRepository.countTotalNumberOfSessionTransactions();
    long totalNumberOfPendingSessionTransactions = adminSessionTransactionStatisticJpaRepository.countTotalNumberOfSessionTransactions(TransactionStatus.PENDING);
    long totalNumberOfSuccessfulSessionTransactions = adminSessionTransactionStatisticJpaRepository.countTotalNumberOfSessionTransactions(TransactionStatus.SUCCESS);
    long totalNumberOfFailedSessionTransactions = adminSessionTransactionStatisticJpaRepository.countTotalNumberOfSessionTransactions(TransactionStatus.FAILED);
    long totalNumberOfCanceledSessionTransactions = adminSessionTransactionStatisticJpaRepository.countTotalNumberOfSessionTransactions(TransactionStatus.CANCELLED);
    long totalNumberOfRefundedSessionTransactions = adminSessionTransactionStatisticJpaRepository.countTotalNumberOfSessionTransactions(TransactionStatus.REFUNDED);
    return SessionTransactionStatistic.builder()
      .totalNumberOfSessionTransactions(totalNumberOfSessionTransactions)
      .totalNumberOfPendingSessionTransactions(totalNumberOfPendingSessionTransactions)
      .totalNumberOfSuccessfulSessionTransactions(totalNumberOfSuccessfulSessionTransactions)
      .totalNumberOfFailedSessionTransactions(totalNumberOfFailedSessionTransactions)
      .totalNumberOfCanceledSessionTransactions(totalNumberOfCanceledSessionTransactions)
      .totalNumberOfRefundedSessionTransactions(totalNumberOfRefundedSessionTransactions)
      .build();
  }

  @Override
  public MemberStatistic getMemberStatistics() {
    long totalNumberOfMembers = adminMemberStatisticJpaRepository.countTotalNumberOfMembers();
    long totalNumberOfUsers = adminMemberStatisticJpaRepository.countTotalNumberOfMembers(ProfileType.USER);
    long totalNumberOfProfessionals = adminMemberStatisticJpaRepository.countTotalNumberOfMembers(ProfileType.PROFESSIONAL);
    long totalNumberOfBusinesses = adminMemberStatisticJpaRepository.countTotalNumberOfMembers(ProfileType.BUSINESS);
    long totalNumberOfMales = adminMemberStatisticJpaRepository.countTotalNumberOfMembersByGender(MemberGender.MALE);
    long totalNumberOfFemales = adminMemberStatisticJpaRepository.countTotalNumberOfMembersByGender(MemberGender.FEMALE);
    long totalNumberOfApprovedMembers = adminMemberStatisticJpaRepository.countTotalNumberOfMembersByVerificationStatus(ProfileVerificationStatus.APPROVED);
    long totalNumberOfPendingMembers = adminMemberStatisticJpaRepository.countTotalNumberOfMembersByVerificationStatus(ProfileVerificationStatus.PENDING);
    long totalNumberOfInProgressMembers = adminMemberStatisticJpaRepository.countTotalNumberOfMembersByVerificationStatus(ProfileVerificationStatus.IN_PROGRESS);
    long totalNumberOfDisapprovedMembers = adminMemberStatisticJpaRepository.countTotalNumberOfMembersByVerificationStatus(ProfileVerificationStatus.DISAPPROVED);
    long totalNumberOfEmailVerifiedMembers = adminMemberStatisticJpaRepository.countTotalNumberOfMembersByEmailAddressVerified(true);
    long totalNumberOfPhoneVerifiedMembers = adminMemberStatisticJpaRepository.countTotalNumberOfMembersByPhoneVerified(true);
    return MemberStatistic.builder()
      .totalNumberOfMembers(totalNumberOfMembers)
      .totalNumberOfUsers(totalNumberOfUsers)
      .totalNumberOfBusinesses(totalNumberOfBusinesses)
      .totalNumberOfProfessionals(totalNumberOfProfessionals)
      .totalNumberOfMales(totalNumberOfMales)
      .totalNumberOfFemales(totalNumberOfFemales)
      .totalNumberOfApprovedMembers(totalNumberOfApprovedMembers)
      .totalNumberOfPendingMembers(totalNumberOfPendingMembers)
      .totalNumberOfInProgressMembers(totalNumberOfInProgressMembers)
      .totalNumberOfDisapprovedMembers(totalNumberOfDisapprovedMembers)
      .totalNumberOfEmailVerifiedMembers(totalNumberOfEmailVerifiedMembers)
      .totalNumberOfPhoneVerifiedMembers(totalNumberOfPhoneVerifiedMembers)
      .build();
  }

  @Override
  public GeneralStatistic getGeneralStatistics() {
    long totalNumberOfMembers = memberJpaRepository.count();
    long totalNumberOfHealthSessions = adminHealthSessionStatisticJpaRepository.count();
    long totalNumberOfSessionTransactions = adminSessionTransactionStatisticJpaRepository.count();
    long totalNumberOfCountries = countryJpaRepository.count();
    long totalNumberOfRoles = roleJpaRepository.count();
    long totalNumberOfMemberStatuses = memberStatusJpaRepository.count();
    long totalNumberOfProfessionalProfiles = professionalJpaRepository.count();
    long totalNumberOfBusinessProfiles = businessJpaRepository.count();
    long totalNumberOfProfessionalAvailability = professionalAvailabilityJpaRepository.count();
    long totalNumberOfProfileVerificationMessage = profileVerificationMessageJpaRepository.count();
    long totalNumberOfProfileVerificationHistory = verificationHistoryJpaRepository.count();
    long totalNumberOfHealthSessionReviews = healthSessionReviewJpaRepository.count();
    long totalNumberOfVerificationDocuments = verificationDocumentJpaRepository.count();
    return GeneralStatistic.builder()
      .totalNumberOfMembers(totalNumberOfMembers)
      .totalNumberOfHealthSessions(totalNumberOfHealthSessions)
      .totalNumberOfSessionTransactions(totalNumberOfSessionTransactions)
      .totalNumberOfCountries(totalNumberOfCountries)
      .totalNumberOfRoles(totalNumberOfRoles)
      .totalNumberOfMemberStatuses(totalNumberOfMemberStatuses)
      .totalNumberOfProfessionalProfiles(totalNumberOfProfessionalProfiles)
      .totalNumberOfBusinessProfiles(totalNumberOfBusinessProfiles)
      .totalNumberOfProfessionalAvailability(totalNumberOfProfessionalAvailability)
      .totalNumberOfProfileVerificationMessage(totalNumberOfProfileVerificationMessage)
      .totalNumberOfProfileVerificationHistory(totalNumberOfProfileVerificationHistory)
      .totalNumberOfHealthSessionReviews(totalNumberOfHealthSessionReviews)
      .totalNumberOfVerificationDocuments(totalNumberOfVerificationDocuments)
      .build();
  }
}
