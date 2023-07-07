package com.umulam.fleen.health.model.request;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PreVerificationRequest {

  private String code;
  private String emailAddress;
  private String phoneNumber;
}
