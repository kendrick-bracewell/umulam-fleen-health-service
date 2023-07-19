package com.umulam.fleen.health.model.event.paystack.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaystackWebhookEvent {

  private String event;
}
