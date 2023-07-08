package com.umulam.fleen.health.model.request;

import com.umulam.fleen.health.constant.verification.ProfileVerificationStatus;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.ProfileVerificationMessage;
import com.umulam.fleen.health.model.domain.Role;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompleteUserSignUpRequest {

  private Member member;
  private Role role;
  private ProfileVerificationMessage verificationMessage;
  private ProfileVerificationStatus profileVerificationStatus;
}
