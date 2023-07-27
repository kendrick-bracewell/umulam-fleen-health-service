package com.umulam.fleen.health.model.mapper;

import com.umulam.fleen.health.model.domain.HealthSessionReview;
import com.umulam.fleen.health.model.view.healthsession.HealthSessionReviewView;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.umulam.fleen.health.util.StringUtil.getFullName;
import static java.util.Objects.nonNull;

public class HealthSessionReviewMapper {

  private HealthSessionReviewMapper() { }

  public static HealthSessionReviewView toHealthSessionReviewView(HealthSessionReview entry) {
    if (nonNull(entry)) {
      return HealthSessionReviewView.builder()
        .review(entry.getReview())
        .ratingName(entry.getRating().name())
        .ratingValue(addOneToRating(entry.getRating().ordinal()))
        .professionalName(getFullName(entry.getProfessional().getFirstName(), entry.getProfessional().getLastName()))
        .createdOn(entry.getCreatedOn())
        .build();
    }
    return null;
  }

  public static HealthSessionReviewView toHealthSessionReviewViewProfessional(HealthSessionReview entry) {
    if (nonNull(entry)) {
      return HealthSessionReviewView.builder()
        .review(entry.getReview())
        .ratingName(entry.getRating().name())
        .ratingValue(addOneToRating(entry.getRating().ordinal()))
        .createdOn(entry.getCreatedOn())
        .build();
    }
    return null;
  }

  public static List<HealthSessionReviewView> toHealthSessionReviewViews(List<HealthSessionReview> entries) {
    if (nonNull(entries) && !entries.isEmpty()) {
      return entries
              .stream()
              .filter(Objects::nonNull)
              .map(HealthSessionReviewMapper::toHealthSessionReviewView)
              .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

  public static List<HealthSessionReviewView> toHealthSessionReviewProfessionalViews(List<HealthSessionReview> entries) {
    if (nonNull(entries) && !entries.isEmpty()) {
      return entries
        .stream()
        .filter(Objects::nonNull)
        .map(HealthSessionReviewMapper::toHealthSessionReviewViewProfessional)
        .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

  public static int addOneToRating(int rating) {
    return rating + 1;
  }
}
