package com.umulam.fleen.health.model.view.member;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.umulam.fleen.health.model.view.member.MemberView;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberViewBasic extends MemberView {
}
