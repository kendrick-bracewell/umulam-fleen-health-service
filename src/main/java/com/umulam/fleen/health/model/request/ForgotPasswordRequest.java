package com.umulam.fleen.health.model.request;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordRequest {

  private String code;
  private String firstName;
  private String lastName;
  private String emailAddress;
  private String phoneNumber;
}
