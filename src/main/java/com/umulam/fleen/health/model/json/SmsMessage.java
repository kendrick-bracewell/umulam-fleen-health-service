package com.umulam.fleen.health.model.json;

import com.umulam.fleen.health.constant.VerificationMessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SmsMessage {

  private final VerificationMessageType title;
  private final String body;
}
