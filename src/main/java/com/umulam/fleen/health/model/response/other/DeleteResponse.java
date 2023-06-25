package com.umulam.fleen.health.model.response.other;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class DeleteResponse {

  private final String message;
  private final String timestamp;
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
