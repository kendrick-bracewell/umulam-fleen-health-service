package com.umulam.fleen.health.model.response.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.umulam.fleen.health.util.DateFormatUtil.DATE_TIME;

@Getter
@Setter
public class SendUpdateEmailAddressOrPhoneNumberVerificationCodeResponse {

  private final String message;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME)
  private LocalDateTime timestamp;

  public SendUpdateEmailAddressOrPhoneNumberVerificationCodeResponse(String message) {
    this.message = message;
    this.timestamp = LocalDateTime.now();
  }
}
