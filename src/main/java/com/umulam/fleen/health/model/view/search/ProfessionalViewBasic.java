package com.umulam.fleen.health.model.view.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.umulam.fleen.health.model.view.ProfessionalView;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfessionalViewBasic extends ProfessionalView {
}
