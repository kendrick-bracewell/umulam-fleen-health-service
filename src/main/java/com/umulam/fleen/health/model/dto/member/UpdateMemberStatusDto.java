package com.umulam.fleen.health.model.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.MemberStatusType;
import com.umulam.fleen.health.validator.EnumValid;
import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberStatusDto {

  @NotNull(message = "{memberStatus.notNull}")
  @EnumValid(enumClass = MemberStatusType.class, message = "{profile.memberStatus}")
  @JsonProperty("member_status")
  private String memberStatus;
}
