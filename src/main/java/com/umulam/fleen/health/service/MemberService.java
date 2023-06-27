package com.umulam.fleen.health.service;

import com.umulam.fleen.health.model.domain.Member;
import lombok.NonNull;

public interface MemberService {

  Member getMemberByEmailAddress(String emailAddress);

  Member save(Member member);

  boolean isMemberExists(@NonNull String emailAddress);
}
