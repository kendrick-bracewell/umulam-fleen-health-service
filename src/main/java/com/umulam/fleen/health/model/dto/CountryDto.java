package com.umulam.fleen.health.model.dto;

import com.umulam.fleen.health.model.domain.Country;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CountryDto {

  @NotBlank(message = "{country.title.notEmpty}")
  @Size(min = 1, max = 100, message = "{country.title.size}")
  private String title;

  @NotBlank(message = "{country.code.notEmpty}")
  @Size(min = 2, max = 5, message = "{country.code.size}")
  private String code;

  @URL(message = "{country.mapLogoUrl.isUrl}")
  @Size(max = 500, message = "{country.mapLogoUrl.size}")
  private String mapLogoUrl;

  public Country toCountry() {
    return Country.builder()
            .title(title)
            .code(code)
            .build();
  }
}
