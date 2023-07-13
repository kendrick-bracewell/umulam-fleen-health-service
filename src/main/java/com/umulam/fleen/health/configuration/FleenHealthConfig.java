package com.umulam.fleen.health.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umulam.fleen.health.resolver.impl.SearchParamArgResolver;
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
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@PropertySource(value = "classpath:application.properties")
@ComponentScan(value = {"com.umulam.fleen.health.controller"})
public class FleenHealthConfig implements WebMvcConfigurer {

  private final SearchParamArgResolver queryParamResolver;

  public FleenHealthConfig(@Lazy SearchParamArgResolver queryParamResolver) {
    this.queryParamResolver = queryParamResolver;
  }

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

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(queryParamResolver);
  }
}
