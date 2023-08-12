package com.umulam.fleen.health.model.dto.business;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.model.domain.Business;
import com.umulam.fleen.health.model.domain.Country;
import com.umulam.fleen.health.validator.CountryExist;
import com.umulam.fleen.health.validator.IsNumber;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBusinessDetailDto {

  @NotBlank(message = "{business.name.notEmpty}")
  @Size(min = 1, max = 500, message = "{business.name.size}")
  private String name;

  @Size(max = 2500, message = "{business.description.size}")
  private String description;

  @NotBlank(message = "{business.contactAddress.notEmpty}")
  @Size(min = 1, max = 500, message = "{business.contactAddress.size}")
  @JsonProperty("contact_address")
  private String contactAddress;

  @NotBlank(message = "{business.registrationNumberOrId.notEmpty}")
  @Size(min = 1, max = 50, message = "{business.registrationNumberOrId.size}")
  @JsonProperty("registration_number_or_id")
  private String registrationNumberOrId;

  @NotBlank(message = "{business.city.notEmpty}")
  @Size(min = 1, max = 200, message = "{business.city.size}")
  private String city;

  @Size(min = 1, max = 200, message = "{business.websiteLink.size}")
  @JsonProperty("website_link")
  private String websiteLink;

  @NotBlank(message = "{business.country.notEmpty}")
  @IsNumber(message = "{business.country.isNumber}")
  @CountryExist(message = "{business.country.exists}")
  private String country;

  public Business toBusiness() {
    return Business.builder()
            .name(name)
            .description(description)
            .contactAddress(contactAddress)
            .registrationNumberOrId(registrationNumberOrId)
            .city(city)
            .websiteLink(websiteLink)
            .country(Country.builder()
                    .id(Integer.parseInt(country)).build())
            .build();

  }

}
