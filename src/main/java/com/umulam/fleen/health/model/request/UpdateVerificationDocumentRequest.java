package com.umulam.fleen.health.model.request;

import com.umulam.fleen.health.constant.verification.VerificationDocumentType;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVerificationDocumentRequest {

  private VerificationDocumentType verificationDocumentType;
  private String documentLink;
}
 
