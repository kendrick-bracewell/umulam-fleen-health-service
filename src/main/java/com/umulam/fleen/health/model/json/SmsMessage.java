package com.umulam.fleen.health.model.json;

import com.umulam.fleen.health.constant.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SmsMessage {

  private final MessageType title;
  private final String message;
}
