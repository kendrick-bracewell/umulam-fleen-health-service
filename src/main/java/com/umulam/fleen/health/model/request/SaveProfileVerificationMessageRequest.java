package com.umulam.fleen.health.model.request;

import com.umulam.fleen.health.constant.verification.ProfileVerificationMessageType;
import com.umulam.fleen.health.constant.verification.ProfileVerificationStatus;
import com.umulam.fleen.health.model.domain.Member;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveProfileVerificationMessageRequest {

  private ProfileVerificationMessageType verificationMessageType;
  private ProfileVerificationStatus verificationStatus;
  private Member member;
  private String emailAddress;
  private String comment;
}
