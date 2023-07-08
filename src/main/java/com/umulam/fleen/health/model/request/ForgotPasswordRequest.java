package com.umulam.fleen.health.model.request;

import com.umulam.fleen.health.constant.authentication.VerificationType;
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
  private VerificationType verificationType;
}
