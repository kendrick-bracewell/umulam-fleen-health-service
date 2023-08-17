package com.umulam.fleen.health.model.response.other;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static com.umulam.fleen.health.util.DateFormatUtil.DATE_TIME;

@Getter
@AllArgsConstructor
public class EntityExistsResponse {

  private boolean exists;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME)
  private LocalDateTime timestamp;

  @JsonProperty("status_code")
  private Integer statusCode;

  public EntityExistsResponse(boolean exists) {
    this(exists, true);
  }

  public EntityExistsResponse(boolean exists, boolean status) {
    this.exists = exists;
    this.timestamp = LocalDateTime.now();
    this.statusCode = status ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value();
  }
}
