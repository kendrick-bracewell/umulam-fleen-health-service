package com.umulam.fleen.health.model.dto.authentication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtTokenDetails {

  private String sub;
  private String[] authorities;
  private Long userId;
  private String status;
  private String firstName;
  private String lastName;
  private String phoneNumber;
}
