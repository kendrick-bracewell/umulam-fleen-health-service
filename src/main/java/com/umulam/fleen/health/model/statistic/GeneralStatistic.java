package com.umulam.fleen.health.model.statistic;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeneralStatistic {

  private long totalNumberOfMembers;
  private long totalNumberOfHealthSessions;
  private long totalNumberOfSessionTransactions;
  private long totalNumberOfCountries;
  private long totalNumberOfRoles;
  private long totalNumberOfMemberStatuses;
  private long totalNumberOfProfessionalProfiles;
  private long totalNumberOfBusinessProfiles;
  private long totalNumberOfProfessionalAvailability;
  private long totalNumberOfProfileVerificationHistory;
  private long totalNumberOfProfileVerificationMessage;
  private long totalNumberOfHealthSessionReviews;
  private long totalNumberOfVerificationDocuments;
}
