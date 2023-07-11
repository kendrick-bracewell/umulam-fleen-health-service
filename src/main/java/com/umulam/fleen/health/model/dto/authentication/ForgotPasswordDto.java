package com.umulam.fleen.health.model.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.authentication.VerificationType;
import com.umulam.fleen.health.validator.EmailAddressExists;
import com.umulam.fleen.health.validator.EnumValid;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordDto {

  @NotBlank(message = "{signUp.emailAddress.notEmpty}")
  @Size(min = 1, max = 150, message = "{signUp.emailAddress.size}")
  @Email(message = "{signUp.emailAddress.format}")
  @JsonProperty("email_address")
  private String emailAddress;

  @EnumValid(enumClass = VerificationType.class, message = "{verification.type}")
  @JsonProperty("verification_type")
  private String verificationType;

}
