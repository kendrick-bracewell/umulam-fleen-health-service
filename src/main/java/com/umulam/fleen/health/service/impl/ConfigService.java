package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.configuration.HealthSessionConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConfigService {

  private final HealthSessionConfig healthSessionConfig;
  private final String paymentIssuer;

  public ConfigService(HealthSessionConfig healthSessionConfig,
                       @Value("${fleen.health.payment.issuer}") String paymentIssuer) {
    this.healthSessionConfig = healthSessionConfig;
    this.paymentIssuer = paymentIssuer;
  }

  public String getHealthSessionPricingCurrency() {
    return healthSessionConfig.getPricingCurrency();
  }

  public String getHealthSessionPaymentCurrency() {
    return healthSessionConfig.getPaymentCurrency();
  }

  public String getPaymentIssuer() { return paymentIssuer; }

  public String getPaymentIssuingCurrency() {
    return healthSessionConfig.getPaymentCurrency();
  }
}
