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

  @EnumValid(enumClass = VerificationType.class, message = "{verification.type}")
  @JsonProperty("verification_type")
  private String verificationType;
}
