package com.umulam.fleen.health.model.mapper;

import com.umulam.fleen.health.model.domain.MemberStatus;
import com.umulam.fleen.health.model.view.MemberStatusView;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MemberStatusMapper {

  private MemberStatusMapper() {

  }

  public static MemberStatusView toMemberStatusView(@NotNull MemberStatus memberStatus) {
    return MemberStatusView.builder()
            .id(memberStatus.getId())
            .title(memberStatus.getTitle())
            .code(memberStatus.getCode())
            .description(memberStatus.getDescription())
            .createdOn(memberStatus.getCreatedOn())
            .updatedOn(memberStatus.getUpdatedOn())
            .build();
  }

  public static List<MemberStatusView> toMemberStatusViews(List<MemberStatus> memberStatuses) {
    if (memberStatuses != null && !memberStatuses.isEmpty()) {
      return memberStatuses
              .stream()
              .filter(Objects::nonNull)
              .map(MemberStatusMapper::toMemberStatusView)
              .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

}
