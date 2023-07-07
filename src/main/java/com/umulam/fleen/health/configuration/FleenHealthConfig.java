package com.umulam.fleen.health.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.*;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@PropertySource(value = "classpath:application.properties")
@ComponentScan(value = {"com.umulam.fleen.health.controller"})
public class FleenHealthConfig {

  @Bean
  public ModelMapper modelMapper() {
    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
    return mapper;
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    objectMapper.findAndRegisterModules();
    return objectMapper;
  }

  @Primary
  @Bean
  public FreeMarkerConfigurationFactoryBean factoryBean() {
    FreeMarkerConfigurationFactoryBean factory = new FreeMarkerConfigurationFactoryBean();
    factory.setTemplateLoaderPath("classpath:/templates/mailing/");
    return factory;
  }

  @Bean
  public SecretGenerator totpSecretGenerator() {
    return new DefaultSecretGenerator();
  }

  @Bean
  public QrGenerator qrGenerator() {
    return new ZxingPngQrGenerator();
  }

  @Bean
  public CodeVerifier codeVerifier() {
    return new DefaultCodeVerifier(new DefaultCodeGenerator(HashingAlgorithm.SHA256), new SystemTimeProvider());
  }

  @Bean
  public WebClient webClient() {
    return WebClient.create();
  }
}
