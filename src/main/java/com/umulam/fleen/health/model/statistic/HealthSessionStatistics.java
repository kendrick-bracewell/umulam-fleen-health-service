package com.umulam.fleen.health.model.statistic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HealthSessionStatistics {

  @JsonProperty("total_number_of_sessions")
  private long totalNumberOfSessions;

  @JsonProperty("total_number_of_pending_sessions")
  private long totalNumberOfPendingSessions;

  @JsonProperty("total_number_of_completed_sessions")
  private long totalNumberOfCompletedSessions;

  @JsonProperty("total_number_scheduled_sessions")
  private long totalNumberOfScheduleSessions;

  @JsonProperty("total_number_rescheduled_sessions")
  private long totalNumberOfRescheduledSessions;
}
