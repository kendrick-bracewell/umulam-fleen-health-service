package com.umulam.fleen.health.model.dto.authentication;

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

  @NotBlank(message = "{signUp.emailAddress.notEmpty}")
  @Size(min = 1, max = 150, message = "{signUp.emailAddress.size}")
  @Email(message = "{signUp.emailAddress.format}")
  private String emailAddress;

  @NotBlank(message = "{signUp.password.notEmpty}")
  @Size(min = 1, max = 24, message = "{signUp.password.size}")
  private String password;
}
