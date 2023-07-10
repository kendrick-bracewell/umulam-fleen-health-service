package com.umulam.fleen.health.model.response.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberGetUpdateDetailsResponse {

  @JsonProperty("first_name")
  private String firstName;

  @JsonProperty("last_name")
  private String lastName;

  @JsonProperty("email_address")
  private String emailAddress;

  @JsonProperty("phone_number")
  private String phoneNumber;
}
