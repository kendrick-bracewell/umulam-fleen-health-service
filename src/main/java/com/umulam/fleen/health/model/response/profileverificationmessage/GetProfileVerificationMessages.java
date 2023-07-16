package com.umulam.fleen.health.model.response.profileverificationmessage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

import static com.umulam.fleen.health.util.DateFormatUtil.DATE;

public interface GetProfileVerificationMessages {
  Integer getId();
  String getTitle();
  String getVerificationMessageType();

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE)
  @JsonProperty("created_on")
  LocalDateTime getCreatedOn();

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE)
  @JsonProperty("updated_on")
  LocalDateTime getUpdatedOn();

}
