package com.umulam.fleen.health.model.dto.verification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.verification.ProfileVerificationStatus;
import com.umulam.fleen.health.validator.CountryExists;
import com.umulam.fleen.health.validator.EnumValid;
import com.umulam.fleen.health.validator.IsNumber;
import com.umulam.fleen.health.validator.VerificationMessageTemplateExists;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileVerificationStatusDto {

  @EnumValid(enumClass = ProfileVerificationStatus.class, message = "{profile.verificationStatus}")
  @JsonProperty("verification_status")
  private String verificationStatus;

  @NotBlank(message = "{profile.verificationTemplate.notEmpty}")
  @IsNumber(message = "{profile.verificationTemplate.isNumber}")
  @VerificationMessageTemplateExists(message = "{profile.verificationTemplate.exists}")
  @JsonProperty("verification_message_template_id")
  private String verificationMessageTemplateId;

  @Size(max = 500, message = "{profile.verification.comment}")
  private String comment;
}
