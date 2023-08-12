package com.umulam.fleen.health.model.dto.country;

import com.umulam.fleen.health.model.domain.Country;
import com.umulam.fleen.health.validator.CountryCodeExist;
import lombok.*;

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
  @CountryCodeExist(message = "{country.code.exists}")
  private String code;

  public Country toCountry() {
    return Country.builder()
            .title(title)
            .code(code)
            .build();
  }
}
