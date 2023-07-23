package com.umulam.fleen.health.model.view.healthsession;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.model.view.base.FleenHealthView;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HealthSessionReviewView extends FleenHealthView {

  private String review;

  @JsonProperty("rating_name")
  private String ratingName;

  @JsonProperty("rating_value")
  private int ratingValue;

  @JsonProperty("professional_name")
  private String professionalName;
}
