package com.umulam.fleen.health.model.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.validator.MobilePhoneNumber;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmUpdatePhoneNumberDto {

  @NotBlank(message = "{verification.code.notEmpty}")
  @Size(min = 1, max = 6, message = "{verification.code.size}")
  private String code;

  @Size(min = 4, max = 15, message = "{signUp.phoneNumber.size}")
  @MobilePhoneNumber(message = "{signUp.phoneNumber.format}")
  @JsonProperty("phone_number")
  private String phoneNumber;
}
 
