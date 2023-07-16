package com.umulam.fleen.health.model.dto.profileverificationmessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.authentication.MfaType;
import com.umulam.fleen.health.validator.EnumValid;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileVerificationMessageDto {

  @NotBlank(message = "{profileVerificationMessage.title.notEmpty}")
  @Size(min = 15, max = 300, message = "{profileVerificationMessage.title.size}")
  private String title;

  @EnumValid(enumClass = MfaType.class, message = "{profileVerificationMessage.type.invalid}")
  @JsonProperty("verification_message_type")
  private String verificationMessageType;

  @NotBlank(message = "{profileVerificationMessage.message.notEmpty}")
  private String message;

  @NotBlank(message = "{profileVerificationMessage.htmlMessage.notEmpty}")
  @JsonProperty("html_message")
  private String htmlMessage;

  @NotBlank(message = "{profileVerificationMessage.plainText.notEmpty}")
  @JsonProperty("plain_text")
  private String plainText;
}
