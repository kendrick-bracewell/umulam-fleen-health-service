package com.umulam.fleen.health.adapter.google.recaptcha.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ReCaptchaResponse {

  private boolean success;
  private String hostname;
  private String action;
  private float score;

  @JsonProperty("challenge_ts")
  private LocalDateTime challengeTimeStamp;

  @JsonProperty("error-codes")
  private List<String> errorCodes;
}
