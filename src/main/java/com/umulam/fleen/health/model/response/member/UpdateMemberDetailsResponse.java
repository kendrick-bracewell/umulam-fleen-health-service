package com.umulam.fleen.health.model.response.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.SUCCESS_MESSAGE;
import static com.umulam.fleen.health.util.DateFormatUtil.DATE_TIME_FORMAT;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberDetailsResponse {

  @JsonProperty("first_name")
  private String firstName;

  @JsonProperty("last_name")
  private String lastName;

  @Builder.Default
  private String message;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
  private String timestamp;

  @JsonProperty("status_code")
  private Integer statusCode;

  public UpdateMemberDetailsResponse(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.message = SUCCESS_MESSAGE;
    this.timestamp = LocalDateTime.now().toString();
    this.statusCode = HttpStatus.OK.value();
  }
}
