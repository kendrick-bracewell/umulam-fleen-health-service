package com.umulam.fleen.health.model.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.base.ProfileType;
import com.umulam.fleen.health.constant.authentication.VerificationType;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.validator.*;
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
  @JsonProperty("first_name")
  private String firstName;

  @NotBlank(message = "{signUp.lastName.notEmpty}")
  @Size(min = 1, max = 100, message = "{signUp.lastName.size}")
  @JsonProperty("last_name")
  private String lastName;

  @NotBlank(message = "{signUp.emailAddress.notEmpty}")
  @Size(min = 1, max = 150, message = "{signUp.emailAddress.size}")
  @Email(message = "{signUp.emailAddress.format}")
  @EmailAddressExists(message = "{signUp.emailAddress.exists}")
  @JsonProperty("email_address")
  private String emailAddress;

  @NotBlank(message = "{signUp.phoneNumber.notEmpty}")
  @Size(min = 4, max = 15, message = "{signUp.phoneNumber.size}")
  @MobilePhoneNumber(message = "{signUp.phoneNumber.format}")
  @PhoneNumberExists(message = "{signUp.phoneNumber.exists}")
  @JsonProperty("phone_number")
  private String phoneNumber;

  @NotBlank(message = "{signUp.password.notEmpty}")
  @Size(min = 8, max = 24, message = "{signUp.password.size}")
  @PasswordValid(message = "{password.constraint}")
  private String password;

  @NotBlank(message = "{signUp.confirmPassword.notEmpty}")
  @Size(min = 8, max = 24, message = "{signUp.confirmPassword.size}")
  @PasswordValid(message = "{password.constraint}")
  @JsonProperty("confirm_password")
  private String confirmPassword;

  @JsonProperty("verification_type")
  @EnumValid(enumClass = VerificationType.class, message = "{verification.type}")
  private String verificationType;

  @JsonProperty("profile_type")
  @EnumValid(enumClass = ProfileType.class, message = "{platform.entity.type.type}")
  private String profileType;

  public Member toMember() {
    return Member.builder()
            .firstName(firstName)
            .lastName(lastName)
            .emailAddress(emailAddress)
            .phoneNumber(phoneNumber)
            .password(password)
            .userType(ProfileType.valueOf(profileType))
            .build();
  }
}
