package com.umulam.fleen.health.model.view.healthsession;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HealthSessionViewBasic extends HealthSessionView {
}
