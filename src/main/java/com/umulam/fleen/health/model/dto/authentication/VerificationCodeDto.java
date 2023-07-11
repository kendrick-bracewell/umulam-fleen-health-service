package com.umulam.fleen.health.model.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.authentication.VerificationType;
import com.umulam.fleen.health.validator.EnumValid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class VerificationCodeDto {

  @NotBlank(message = "{verification.code.notEmpty}")
  @Size(min = 1, max = 6, message = "{verification.code.size}")
  private String code;

  @EnumValid(enumClass = VerificationType.class, message = "{verification.type}")
  @JsonProperty("verification_type")
  private String verificationType;
}
