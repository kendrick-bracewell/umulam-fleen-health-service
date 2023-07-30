package com.umulam.fleen.health.model.event.flutterwave.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlutterwaveWebhookEvent {

  private String event;
}
