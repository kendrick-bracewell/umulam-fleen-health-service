package com.umulam.fleen.health.configuration.security;

import com.umulam.fleen.health.filter.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@AllArgsConstructor
public class SecurityConfiguration {

  private final UserDetailsService userDetailsService;
  private final JwtAuthenticationFilter authenticationFilter;

  private static final String[] WHITELIST = {
    "/v2/api-docs",
    "/swagger-resources",
    "/swagger-resources/**",
    "/swagger-ui.html",
    "/v3/api-docs/**",
    "/swagger-ui/**",
    "/auth/public/**",
    "/actuator/health",
    "/auth/**",
          "/**"
  };

  @Bean
  public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf()
            .disable()
            .authorizeRequests()
      .antMatchers(WHITELIST)
        .permitAll();

    http
      .headers()
      .frameOptions()
       .sameOrigin();

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder
      .userDetailsService(userDetailsService)
      .passwordEncoder(passwordEncoder());

    return authenticationManagerBuilder.build();
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*").allowedOriginPatterns("*");
      }
    };
  }

  public void webSecurityCustomizer(WebSecurity web) {
    web.ignoring()
            .antMatchers("/resources/**", "/static/**");
  }

}
