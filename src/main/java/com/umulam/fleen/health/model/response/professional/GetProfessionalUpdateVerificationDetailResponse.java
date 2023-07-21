package com.umulam.fleen.health.model.response.professional;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetProfessionalUpdateVerificationDetailResponse {

  private List<?> countries;
}
