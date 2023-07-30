package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.configuration.HealthSessionConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConfigService {

  private final HealthSessionConfig healthSessionConfig;

  public ConfigService(HealthSessionConfig healthSessionConfig) {
    this.healthSessionConfig = healthSessionConfig;
  }

  public String getPricingCurrency() {
    return healthSessionConfig.getPricingCurrency();
  }

  public String getPaymentCurrency() {
    return healthSessionConfig.getPaymentCurrency();
  }
}
