package com.umulam.fleen.health.model.response.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.member.MemberGender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.SUCCESS_MESSAGE;
import static com.umulam.fleen.health.util.DateFormatUtil.DATE;
import static com.umulam.fleen.health.util.DateFormatUtil.DATE_TIME;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateMemberDetailsResponse {

  @JsonProperty("first_name")
  private String firstName;

  @JsonProperty("last_name")
  private String lastName;

  private String gender;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE)
  private LocalDateTime dateOfBirth;

  @Builder.Default
  private String message;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME)
  private String timestamp;

  @JsonProperty("status_code")
  private Integer statusCode;

  public UpdateMemberDetailsResponse(String firstName, String lastName, MemberGender gender, LocalDateTime dateOfBirth) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.gender = gender.name();
    this.dateOfBirth = dateOfBirth;
    this.message = SUCCESS_MESSAGE;
    this.timestamp = LocalDateTime.now().toString();
    this.statusCode = HttpStatus.OK.value();
  }
}
