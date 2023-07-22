package com.umulam.fleen.health.model.response.healthsession;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static com.umulam.fleen.health.util.DateFormatUtil.DATE_TIME;

@Getter
@Setter
public class ProfessionalCheckAvailabilityResponse {

  private boolean available;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME)
  private final String timestamp;

  @JsonProperty("status_code")
  private Integer statusCode;

  public ProfessionalCheckAvailabilityResponse(boolean available) {
    this.available = available;
    this.timestamp = LocalDateTime.now().toString();
    this.statusCode = HttpStatus.OK.value();
  }
}
