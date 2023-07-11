package com.umulam.fleen.health.model.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.authentication.VerificationType;
import com.umulam.fleen.health.validator.EnumValid;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmailAddressOrPhoneNumberDto {

  @JsonProperty("verification_type")
  @EnumValid(enumClass = VerificationType.class, message = "{verification.type}")
  private String verificationType;
}
