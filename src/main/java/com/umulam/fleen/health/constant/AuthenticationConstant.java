package com.umulam.fleen.health.constant;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import static com.umulam.fleen.health.util.DateTimeUtil.getTimeInMillis;

@Getter
@Component
@PropertySource("classpath:application.properties")
public final class AuthenticationConstant {

  @Value("${umulam.fleen.health.jwt.secret}")
  public static String JWT_SECRET;

  @Value("${umulam.fleen.health.jwt.issuer}")
  public static String JWT_ISSUER;

  public static final String AUTH_CACHE_PREFIX = "JWT_TOKEN:";
  public static final String AUTH_HEADER_KEY = "Authorization";
  public static final String REFRESH_TOKEN_HEADER_KEY = "REFRESH_TOKEN";
  public static final String AUTH_HEADER_PREFIX = "BEARER";
  public static final String TOKEN_TYPE_KEY = "tokenType";
  public static final long ACCESS_TOKEN_VALIDITY = getTimeInMillis(60, 60, 5, 0);
  public static final long REFRESH_TOKEN_VALIDITY = getTimeInMillis(60, 60, 24, 2);
}
