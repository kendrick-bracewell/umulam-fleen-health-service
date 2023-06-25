package com.umulam.fleen.health.model.dto.authentication;

import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.validator.EmailAddressExists;
import com.umulam.fleen.health.validator.FieldMatch;
import lombok.*;

import javax.validation.constraints.Email;
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
public class SignUpDto {

  @NotBlank(message = "{signUp.firstName.notEmpty}")
  @Size(min = 1, max = 100, message = "{signUp.firstName.size}")
  private String firstName;

  @NotBlank(message = "{signUp.lastName.notEmpty}")
  @Size(min = 1, max = 100, message = "{signUp.lastName.size}")
  private String lastName;

  @NotBlank(message = "{signUp.emailAddress.notEmpty}")
  @Size(min = 1, max = 150, message = "{signUp.emailAddress.size}")
  @Email(message = "{signUp.emailAddress.format}")
  @EmailAddressExists(message = "{signUp.emailAddress.exists}")
  private String emailAddress;

  @NotBlank(message = "{signUp.password.notEmpty}")
  @Size(min = 1, max = 24, message = "{signUp.password.size}")
  private String password;

  @NotBlank(message = "{signUp.confirmPassword.notEmpty}")
  @Size(min = 1, max = 24, message = "{signUp.confirmPassword.size}")
  private String confirmPassword;

  public Member toMember() {
    return Member.builder()
            .firstName(firstName)
            .lastName(lastName)
            .emailAddress(emailAddress)
            .password(password)
            .build();
  }
}
