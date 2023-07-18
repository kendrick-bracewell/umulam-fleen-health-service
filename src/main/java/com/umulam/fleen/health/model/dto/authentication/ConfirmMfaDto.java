package com.umulam.fleen.health.model.dto.authentication;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.authentication.MfaType;
import com.umulam.fleen.health.validator.EnumValid;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmMfaDto {

  @NotBlank(message = "{verification.code.notEmpty}")
  @Size(min = 1, max = 6, message = "{verification.code.size}")
  private String code;

  @NotNull(message = "{mfa.type.notNull}")
  @EnumValid(enumClass = MfaType.class, message = "{mfa.type.type}")
  @JsonProperty("mfa_type")
  private String mfaType;
}
