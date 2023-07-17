package com.umulam.fleen.health.model.response.authentication;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

import static com.umulam.fleen.health.util.DateFormatUtil.DATE_TIME;

@Getter
@AllArgsConstructor
public class CreateEncodedPasswordResponse {

  private String encodedPassword;
  private String rawPassword;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME)
  private LocalDateTime timestamp;

  public CreateEncodedPasswordResponse(String encodedPassword, String rawPassword) {
    this.encodedPassword = encodedPassword;
    this.rawPassword = rawPassword;
    this.timestamp = LocalDateTime.now();
  }
}
