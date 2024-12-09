package com.umulam.fleen.health.model.view.country;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.umulam.fleen.health.model.view.country.CountryView;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountryViewBasic extends CountryView {
}
 
