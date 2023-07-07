package com.umulam.fleen.health.model.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.validator.FieldMatch;
import com.umulam.fleen.health.validator.PasswordValid;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldMatch.List({
        @FieldMatch(first = "password", second = "confirmPassword", message = "{signUp.passwordConfirmation.equal}")
})
public class ChangePasswordDto {

  @NotBlank(message = "{signUp.password.notEmpty}")
  @Size(min = 8, max = 24, message = "{signUp.password.size}")
  @PasswordValid(message = "{password.constraint}")
  private String password;

  @NotBlank(message = "{signUp.confirmPassword.notEmpty}")
  @Size(min = 8, max = 24, message = "{signUp.confirmPassword.size}")
  @PasswordValid(message = "{password.constraint}")
  @JsonProperty("confirm_password")
  private String confirmPassword;

}
