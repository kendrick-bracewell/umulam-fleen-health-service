package com.umulam.fleen.health.model.response.professional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.model.view.ProfessionalAvailabilityView;
import com.umulam.fleen.health.model.view.ProfessionalScheduleHealthSessionView;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class GetProfessionalBookSessionResponse {

  @JsonProperty("availability_periods")
  private List<ProfessionalAvailabilityView> availabilityPeriods;

  @JsonProperty("scheduled_sessions")
  private List<ProfessionalScheduleHealthSessionView> scheduledSessions;
}
