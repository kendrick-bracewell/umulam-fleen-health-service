package com.umulam.fleen.health.model.response.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.SUCCESS_MESSAGE;
import static com.umulam.fleen.health.util.DateFormatUtil.DATE_TIME_FORMAT;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateEmailAddressOrPhoneNumberResponse {

  @JsonProperty("email_address")
  private String emailAddress;

  @JsonProperty("phone_number")
  private String phoneNumber;

  @Builder.Default
  private String message;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
  private String timestamp;

  @JsonProperty("status_code")
  private Integer statusCode;

  public UpdateEmailAddressOrPhoneNumberResponse(String emailAddress, String phoneNumber) {
    this.emailAddress = emailAddress;
    this.phoneNumber = phoneNumber;
    this.message = SUCCESS_MESSAGE;
    this.timestamp = LocalDateTime.now().toString();
    this.statusCode = HttpStatus.OK.value();
  }
}
