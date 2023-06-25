package com.umulam.fleen.health.service;

import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.dto.authentication.SignUpDto;
import lombok.NonNull;

public interface MemberService {

  Member signup(SignUpDto dto);

  boolean isMemberExists(@NonNull String emailAddress);
}
