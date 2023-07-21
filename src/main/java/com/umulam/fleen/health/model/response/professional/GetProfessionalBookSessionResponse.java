package com.umulam.fleen.health.model.response.professional;

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

  private List<ProfessionalAvailabilityView> availabilityPeriods;
  private List<ProfessionalScheduleHealthSessionView> scheduledSessions;
}
