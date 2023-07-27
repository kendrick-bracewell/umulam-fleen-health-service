package com.umulam.fleen.health.model.dto.healthsession;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.member.ProfessionalQualificationType;
import com.umulam.fleen.health.constant.session.HealthSessionReviewRating;
import com.umulam.fleen.health.validator.EnumValid;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddHealthSessionReview {

  @Size(min = 10, max = 1000, message = "{session.review.review.size}")
  private String note;

  @NotNull(message = "{session.review.rating.notNull}")
  @EnumValid(enumClass = HealthSessionReviewRating.class, message = "{session.review.rating.type}")
  @JsonProperty("review_rating")
  private String reviewRating;
}
