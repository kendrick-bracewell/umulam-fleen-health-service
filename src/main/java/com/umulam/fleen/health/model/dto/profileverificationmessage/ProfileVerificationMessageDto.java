package com.umulam.fleen.health.model.dto.profileverificationmessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.verification.ProfileVerificationMessageType;
import com.umulam.fleen.health.model.domain.ProfileVerificationMessage;
import com.umulam.fleen.health.validator.EnumValid;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileVerificationMessageDto {

  @NotBlank(message = "{profileVerificationMessage.title.notEmpty}")
  @Size(min = 5, max = 300, message = "{profileVerificationMessage.title.size}")
  private String title;

  @NotNull(message = "{profileVerificationMessage.notNull}")
  @EnumValid(enumClass = ProfileVerificationMessageType.class, message = "{profileVerificationMessage.type.invalid}")
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

  public ProfileVerificationMessage toProfileVerificationMessage() {
    return ProfileVerificationMessage.builder()
            .title(title)
            .verificationMessageType(ProfileVerificationMessageType.valueOf(verificationMessageType))
            .message(message)
            .htmlMessage(htmlMessage)
            .plainText(plainText)
            .build();
  }
}
