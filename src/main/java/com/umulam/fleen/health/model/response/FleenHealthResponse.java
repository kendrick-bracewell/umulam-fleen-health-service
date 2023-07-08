package com.umulam.fleen.health.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

import static com.umulam.fleen.health.util.DateFormatUtil.DATE_TIME_FORMAT;

@Getter
@AllArgsConstructor
public class FleenHealthResponse {

  private String message;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
  private LocalDateTime timestamp;

  public FleenHealthResponse(String message) {
    this.message = message;
    this.timestamp = LocalDateTime.now();
  }
}
