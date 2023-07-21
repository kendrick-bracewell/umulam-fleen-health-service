package com.umulam.fleen.health.model.response.professional;

import com.umulam.fleen.health.model.view.ProfessionalAvailabilityView;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class GetBookSessionResponse {

  private List<ProfessionalAvailabilityView> availability;
  private List<>
}
