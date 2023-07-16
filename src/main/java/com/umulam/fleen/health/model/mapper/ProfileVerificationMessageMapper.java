package com.umulam.fleen.health.model.mapper;

import com.umulam.fleen.health.model.domain.ProfileVerificationMessage;
import com.umulam.fleen.health.model.view.ProfileVerificationMessageView;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProfileVerificationMessageMapper {

  private ProfileVerificationMessageMapper() {}

  public static ProfileVerificationMessageView toProfileVerificationMessageView(ProfileVerificationMessage entry) {
    if (Objects.nonNull(entry)) {
      return ProfileVerificationMessageView.builder()
              .id(entry.getId())
              .title(entry.getTitle())
              .message(entry.getMessage())
              .htmlMessage(entry.getHtmlMessage())
              .plainText(entry.getPlainText())
              .verificationMessageType(entry.getVerificationMessageType().name())
              .createdOn(entry.getCreatedOn())
              .updatedOn(entry.getUpdatedOn())
              .build();
    }
    return null;
  }

}
