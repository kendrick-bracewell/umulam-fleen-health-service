package com.umulam.fleen.health.model.mapper;

import com.umulam.fleen.health.model.domain.MemberStatus;
import com.umulam.fleen.health.model.view.MemberStatusView;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MemberStatusMapper {

  private MemberStatusMapper() {

  }

  public static MemberStatusView toMemberStatusView(MemberStatus entry) {
    if (Objects.nonNull(entry)) {
      return MemberStatusView.builder()
              .id(entry.getId())
              .title(entry.getTitle())
              .code(entry.getCode())
              .description(entry.getDescription())
              .createdOn(entry.getCreatedOn())
              .updatedOn(entry.getUpdatedOn())
              .build();
    }
    return null;
  }

  public static List<MemberStatusView> toMemberStatusViews(List<MemberStatus> entries) {
    if (entries != null && !entries.isEmpty()) {
      return entries
              .stream()
              .filter(Objects::nonNull)
              .map(MemberStatusMapper::toMemberStatusView)
              .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

}
