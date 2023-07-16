package com.umulam.fleen.health.model.mapper;

import com.umulam.fleen.health.constant.member.MemberGender;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.view.MemberView;
import com.umulam.fleen.health.model.view.MemberViewBasic;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MemberMapper {

  private MemberMapper() {}

  public static MemberView toMemberView(Member entry) {
    if (Objects.nonNull(entry)) {
      return MemberView.builder()
              .id(entry.getId())
              .firstName(entry.getFirstName())
              .lastName(entry.getLastName())
              .emailAddress(entry.getEmailAddress())
              .phoneNumber(entry.getPhoneNumber())
              .memberStatus(MemberStatusMapper.toMemberStatusView(entry.getMemberStatus()))
              .mfaType(entry.getMfaType().name())
              .mfaEnabled(entry.isMfaEnabled())
              .profilePhoto(entry.getProfilePhoto())
              .userType(entry.getUserType().name())
              .emailAddressVerified(entry.isEmailAddressVerified())
              .phoneNumberVerified(entry.isPhoneNumberVerified())
              .profileVerificationStatus(entry.getVerificationStatus().name())
              .gender(entry.getGender().name())
              .dateOfBirth(entry.getDateOfBirth())
              .createdOn(entry.getCreatedOn())
              .updatedOn(entry.getUpdatedOn())
              .build();
    }
    return null;
  }

  public static MemberViewBasic toMemberViewBasic(Member entry) {
    if (Objects.nonNull(entry)) {
      return MemberViewBasic.builder()
              .id(entry.getId())
              .firstName(entry.getFirstName())
              .lastName(entry.getLastName())
              .profilePhoto(entry.getProfilePhoto())
              .gender(entry.getGender().name())
              .build();
    }
    return null;
  }

  public static List<MemberView> toMemberViews(List<Member> entries) {
    if (Objects.nonNull(entries) && !entries.isEmpty()) {
      return entries
              .stream()
              .map(MemberMapper::toMemberView)
              .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

}
