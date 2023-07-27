package com.umulam.fleen.health.model.statistic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberStatistic {

  @JsonProperty("total_number_members")
  private long totalNumberOfMembers;

  @JsonProperty("total_number_of_uers")
  private long totalNumberOfUsers;

  @JsonProperty("total_number_of_professionals")
  private long totalNumberOfProfessionals;

  @JsonProperty("total_number_businesses")
  private long totalNumberOfBusinesses;

  @JsonProperty("total_number_of_males")
  private long totalNumberOfMales;

  @JsonProperty("total_number_of_females")
  private long totalNumberOfFemales;

  @JsonProperty("total_number_of_approved_members")
  private long totalNumberOfApprovedMembers;

  @JsonProperty("total_number_of_pending_members")
  private long totalNumberOfPendingMembers;

  @JsonProperty("total_number_in_progress_members")
  private long totalNumberOfInProgressMembers;

  @JsonProperty("total_number_of_disapproved_members")
  private long totalNumberOfDisapprovedMembers;

  @JsonProperty("total_number_of_email_verified_members")
  private long totalNumberOfEmailVerifiedMembers;

  @JsonProperty("total_number_of_phone_verified_members")
  private long totalNumberOfPhoneVerifiedMembers;
}
