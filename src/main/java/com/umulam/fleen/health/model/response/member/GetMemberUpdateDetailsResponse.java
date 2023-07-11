package com.umulam.fleen.health.model.response.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.member.MemberGender;

import java.time.LocalDate;

import static com.umulam.fleen.health.util.DateFormatUtil.DATE;

public interface GetMemberUpdateDetailsResponse {

  @JsonProperty("first_name")
  String getFirstName();

  @JsonProperty("last_name")
  String getLastName();

  @JsonProperty("email_address")
  String getEmailAddress();

  @JsonProperty("phone_number")
  String getPhoneNumber();

  MemberGender getGender();

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE)
  @JsonProperty("date_of_birth")
  LocalDate getDateOfBirth();
}
