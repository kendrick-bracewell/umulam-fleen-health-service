package com.umulam.fleen.health.model.dto.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.base.ProfileType;
import com.umulam.fleen.health.constant.member.MemberGender;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.validator.*;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static org.springframework.util.StringUtils.capitalize;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMemberDto {

  @NotBlank(message = "{signUp.firstName.notEmpty}")
  @Size(min = 2, max = 100, message = "{signUp.firstName.size}")
  @JsonProperty("first_name")
  private String firstName;

  @NotBlank(message = "{signUp.lastName.notEmpty}")
  @Size(min = 2, max = 100, message = "{signUp.lastName.size}")
  @JsonProperty("last_name")
  private String lastName;

  @NotBlank(message = "{signUp.emailAddress.notEmpty}")
  @Size(min = 1, max = 150, message = "{signUp.emailAddress.size}")
  @Email(message = "{signUp.emailAddress.format}")
  @EmailValid(message = "{signUp.emailAddress.format}")
  @EmailAddressExist(message = "{signUp.emailAddress.exists}")
  @JsonProperty("email_address")
  private String emailAddress;

  @NotBlank(message = "{signUp.phoneNumber.notEmpty}")
  @Size(min = 4, max = 15, message = "{signUp.phoneNumber.size}")
  @MobilePhoneNumber(message = "{signUp.phoneNumber.format}")
  @PhoneNumberExists(message = "{signUp.phoneNumber.exists}")
  @JsonProperty("phone_number")
  private String phoneNumber;

  @NotNull(message = "{member.profileType.notNull}")
  @EnumValid(enumClass = ProfileType.class, message = "{member.profileType}")
  @JsonProperty("profile_type")
  private String profileType;

  @NotNull(message = "{member.gender.notNull}")
  @EnumValid(enumClass = MemberGender.class, message = "{signup.gender}")
  @JsonProperty("gender")
  private String gender;

  public Member toMember() {
    return Member.builder()
            .firstName(capitalize(firstName))
            .lastName(capitalize(lastName))
            .emailAddress(emailAddress.toLowerCase())
            .phoneNumber(phoneNumber)
            .userType(ProfileType.valueOf(profileType))
            .gender(MemberGender.valueOf(gender))
            .build();
  }
}
