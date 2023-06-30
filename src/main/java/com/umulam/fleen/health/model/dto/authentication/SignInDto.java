package com.umulam.fleen.health.model.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignInDto {

  @NotBlank(message = "{signIn.emailAddress.notEmpty}")
  @Size(min = 1, max = 150, message = "{signIn.emailAddress.size}")
  @Email(message = "{signIn.emailAddress.format}")
  @JsonProperty("email_address")
  private String emailAddress;

  @NotBlank(message = "{signIn.password.notEmpty}")
  @Size(min = 8, max = 24, message = "{signIn.password.size}")
  private String password;
}
