package com.umulam.fleen.health.model.dto.healthsession;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.session.HealthSessionReviewRating;
import com.umulam.fleen.health.model.domain.HealthSessionReview;
import com.umulam.fleen.health.validator.EnumOrdinalValid;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.umulam.fleen.health.util.EnumUtil.getEnumConstant;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddHealthSessionReviewDto {

  @Size(min = 10, max = 1000, message = "{session.review.review.size}")
  private String review;

  @NotNull(message = "{session.review.rating.notNull}")
  @EnumOrdinalValid(enumClass = HealthSessionReviewRating.class, message = "{session.review.rating.type}")
  @JsonProperty("review_rating")
  private String reviewRating;

  public HealthSessionReview toHealthSessionReview() {
    return HealthSessionReview.builder()
      .review(review)
      .rating(getEnumConstant(HealthSessionReviewRating.class, Integer.parseInt(reviewRating)))
      .build();
  }
}
