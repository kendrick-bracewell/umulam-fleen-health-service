package com.umulam.fleen.health.configuration.security;

import com.umulam.fleen.health.configuration.security.provider.CustomAuthenticationProvider;
import com.umulam.fleen.health.filter.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
@AllArgsConstructor
public class SecurityConfiguration {

  private final JwtAuthenticationFilter authenticationFilter;
  private final CustomAuthenticationProvider authenticationProvider;

  public static final String[] WHITELIST = {
    "/auth/**",
    "/v2/api-docs",
    "/swagger-resources",
    "/swagger-resources/**",
    "/swagger-ui.html",
    "/v3/api-docs/**",
    "/swagger-ui/**",
    "/actuator/health",
    "/**"
  };

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.httpBasic().disable();
    http.formLogin().disable();
    http.csrf().disable();
    http.sessionManagement().sessionCreationPolicy(STATELESS);
    http
        .headers()
        .frameOptions()
        .sameOrigin();

    http
      .authorizeRequests()
      .antMatchers(WHITELIST).permitAll();
    http
      .authorizeRequests()
      .anyRequest().authenticated();

    http
      .addFilterBefore(
        authenticationFilter,
        UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.authenticationProvider(authenticationProvider);

    return authenticationManagerBuilder.build();
  }

  public void webSecurityCustomizer(WebSecurity web) {
    web.ignoring()
            .antMatchers("/resources/**", "/static/**");
  }
}
