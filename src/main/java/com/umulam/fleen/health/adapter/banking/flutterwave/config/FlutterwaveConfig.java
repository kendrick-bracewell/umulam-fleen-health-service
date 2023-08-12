package com.umulam.fleen.health.adapter.banking.flutterwave.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "flutterwave")
public class FlutterwaveConfig {

  @NotBlank
  private String secretKey;

  @NotBlank
  private String publicKey;

  @NotBlank
  private String verificationHeader;

  @NotBlank
  private String secretHash;
}
