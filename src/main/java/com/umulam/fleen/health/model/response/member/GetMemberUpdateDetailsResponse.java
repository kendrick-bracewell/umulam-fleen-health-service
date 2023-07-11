package com.umulam.fleen.health.model.response.member;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface GetMemberUpdateDetailsResponse {

  @JsonProperty("first_name")
  String getFirstName();

  @JsonProperty("last_name")
  String getLastName();

  @JsonProperty("email_address")
  String getEmailAddress();

  @JsonProperty("phone_number")
  String getPhoneNumber();
}
