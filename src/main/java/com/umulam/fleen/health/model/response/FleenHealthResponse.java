package com.umulam.fleen.health.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@AllArgsConstructor
public class FleenHealthResponse {

  private String message;
  private Date timestamp;

  public FleenHealthResponse(String message) {
    this.message = message;
    this.timestamp = new Date();
  }
}
