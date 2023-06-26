package com.umulam.fleen.health.configuration.cache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "fleen.health.cache")
public class CacheCredentials {

  @NotBlank
  private String host;

  @NotBlank
  private Integer port;

  @NotBlank
  private String username;

  @NotBlank
  private String password;

  @NotBlank
  private String prefix;

  @NotBlank
  private Integer ttl;
}
