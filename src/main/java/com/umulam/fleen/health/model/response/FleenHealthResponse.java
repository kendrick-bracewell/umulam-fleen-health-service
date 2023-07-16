package com.umulam.fleen.health.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static com.umulam.fleen.health.util.DateFormatUtil.DATE_TIME_FORMAT;

@Getter
@AllArgsConstructor
public class FleenHealthResponse {

  private String message;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
  private LocalDateTime timestamp;

  @JsonProperty("status_code")
  private Integer statusCode;

  public FleenHealthResponse(String message) {
    this(message, true);
  }

  public FleenHealthResponse(String message, boolean status) {
    this.message = message;
    this.timestamp = LocalDateTime.now();
    this.statusCode = status ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value();
  }
}
