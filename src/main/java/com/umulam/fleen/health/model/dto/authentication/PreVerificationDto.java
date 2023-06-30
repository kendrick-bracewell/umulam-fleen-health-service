package com.umulam.fleen.health.model.dto.authentication;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PreVerificationDto {

  private String code;
  private String emailAddress;
  private String phoneNumber;
}
