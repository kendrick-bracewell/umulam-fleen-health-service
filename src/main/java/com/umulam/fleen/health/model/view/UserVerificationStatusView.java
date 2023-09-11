package com.umulam.fleen.health.model.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

import static com.umulam.fleen.health.util.DateFormatUtil.DATE_TIME;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserVerificationStatusView {

  private String status;
  private String label;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME)
  private LocalDateTime timestamp;

  public UserVerificationStatusView(String status, String label) {
    this.status = status;
    this.label = label;
    this.timestamp = LocalDateTime.now();
  }
}
