package com.umulam.fleen.health.model.response;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupportedCountry {

  private String currency;
  private String countryName;
}
