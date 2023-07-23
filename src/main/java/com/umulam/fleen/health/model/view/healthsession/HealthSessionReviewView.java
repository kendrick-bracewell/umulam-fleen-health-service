package com.umulam.fleen.health.model.view.healthsession;

import com.umulam.fleen.health.model.view.base.FleenHealthView;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HealthSessionReviewView extends FleenHealthView {

  private String review;
  private String professionalName;
  private String ratingName;
  private int ratingValue;
}
