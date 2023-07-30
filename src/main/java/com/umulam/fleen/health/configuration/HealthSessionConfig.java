package com.umulam.fleen.health.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "fleen.health.session")
public class HealthSessionConfig {

  @NotBlank
  private String pricingCurrency;

  @NotBlank
  private String paymentCurrency;
}
