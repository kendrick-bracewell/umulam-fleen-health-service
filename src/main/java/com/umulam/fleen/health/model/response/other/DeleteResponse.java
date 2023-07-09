package com.umulam.fleen.health.model.response.other;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static com.umulam.fleen.health.util.DateFormatUtil.DATE_TIME_FORMAT;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class DeleteResponse {

  private final String message;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
  private final String timestamp;

  @JsonProperty("status_code")
  private Integer statusCode;

  public DeleteResponse(String message, boolean status) {
    this.message = message;
    this.timestamp = LocalDateTime.now().toString();
    this.statusCode = status ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value();
  }

  public DeleteResponse() {
    this("Success", true);
  }
}
