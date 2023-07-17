package com.umulam.fleen.health.model.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.validator.EmailValid;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordDto {

  @NotBlank(message = "{signUp.emailAddress.notEmpty}")
  @Size(min = 1, max = 150, message = "{signUp.emailAddress.size}")
  @Email(message = "{signUp.emailAddress.format}")
  @EmailValid(message = "{signUp.emailAddress.format}")
  @JsonProperty("email_address")
  private String emailAddress;

  @NotBlank(message = "{verification.code.notEmpty}")
  @Size(min = 1, max = 6, message = "{verification.code.size}")
  private String code;
}
