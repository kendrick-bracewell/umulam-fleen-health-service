package com.umulam.fleen.health.model.view.professional;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfessionalViewBasic extends ProfessionalView {
}
