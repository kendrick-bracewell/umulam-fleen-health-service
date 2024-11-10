package com.umulam.fleen.health.model.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.authentication.MfaType;
import com.umulam.fleen.health.validator.EnumValid;
import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MfaTypeDto {

  @NotNull(message = "{mfa.type.notNull}")
  @EnumValid(enumClass = MfaType.class, message = "{mfa.type.type}")
  @JsonProperty("mfa_type")
  private String mfaType;
}
 
