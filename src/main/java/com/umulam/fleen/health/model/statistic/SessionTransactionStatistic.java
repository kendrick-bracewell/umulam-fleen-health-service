package com.umulam.fleen.health.model.statistic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionTransactionStatistic {

  @JsonProperty("total_number_of_sessions")
  private long totalNumberOfSessionTransactions;

  @JsonProperty("total_number_of_pending_session_transactions")
  private long totalNumberOfPendingSessionTransactions;

  @JsonProperty("total_number_of_successful_session_transactions")
  private long totalNumberOfSuccessfulSessionTransactions;

  @JsonProperty("total_number_failed_session_transactions")
  private long totalNumberOfFailedSessionTransactions;

  @JsonProperty("total_number_canceled_session_transactions")
  private long totalNumberOfCanceledSessionTransactions;

  @JsonProperty("total_number_refunded_sessions")
  private long totalNumberOfRefundedSessionTransactions;
}
