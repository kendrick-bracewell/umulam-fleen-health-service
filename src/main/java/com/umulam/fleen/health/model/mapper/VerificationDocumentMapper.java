package com.umulam.fleen.health.model.mapper;

import com.umulam.fleen.health.model.domain.VerificationDocument;
import com.umulam.fleen.health.model.view.VerificationDocumentView;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class VerificationDocumentMapper {

  private VerificationDocumentMapper() {}

  public static VerificationDocumentView toVerificationDocumentView(VerificationDocument entry) {
    if (Objects.nonNull(entry)) {
      return VerificationDocumentView.builder()
              .id(entry.getId())
              .documentType(entry.getVerificationDocumentType().name())
              .filename(entry.getFilename())
              .link(entry.getLink())
              .createdOn(entry.getCreatedOn())
              .updatedOn(entry.getUpdatedOn())
              .build();
    }
    return null;
  }

  public static List<VerificationDocumentView> toVerificationDocumentViews(List<VerificationDocument> entries) {
    if (Objects.nonNull(entries) && !entries.isEmpty()) {
      return entries
              .stream()
              .filter(Objects::nonNull)
              .map(VerificationDocumentMapper::toVerificationDocumentView)
              .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }
}
