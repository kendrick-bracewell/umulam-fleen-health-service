package com.umulam.fleen.health.model.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.authentication.VerificationType;
import com.umulam.fleen.health.validator.EnumValid;
import com.umulam.fleen.health.validator.MobilePhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ResendVerificationCodeDto {

  @Email(message = "{signUp.emailAddress.format}")
  @JsonProperty("email_address")
  private String emailAddress;

  @Size(min = 4, max = 15, message = "{signUp.phoneNumber.size}")
  @MobilePhoneNumber(message = "{signUp.phoneNumber.format}")
  @JsonProperty("phone_number")
  private String phoneNumber;

  @EnumValid(enumClass = VerificationType.class, message = "{verification.type}")
  @JsonProperty("verification_type")
  private String verificationType;
}
