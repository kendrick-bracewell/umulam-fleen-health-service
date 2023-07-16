package com.umulam.fleen.health.constant.authentication;

import lombok.Getter;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import static com.umulam.fleen.health.util.DateTimeUtil.getTimeInMillis;

@Getter
@Component
@PropertySource("classpath:application.properties")
public final class AuthenticationConstant {

  public static final String MFA_SECRET_LABEL = "start@fleenhealth.com";
  public static final String MFA_SECRET_ISSUER = "Fleen Health";
  public static final String AUTH_CACHE_PREFIX = "JWT_TOKEN:::";
  public static final String AUTH_REFRESH_CACHE_PREFIX = "JWT_REFRESH_TOKEN:::";
  public static final String RESET_PASSWORD_CACHE_PREFIX = "RESET_PASSWORD_TOKEN:::";
  public static final String MFA_SETUP_EMAIL_CACHE_PREFIX = "MFA_SETUP_EMAIL:::";
  public static final String MFA_SETUP_PHONE_CACHE_PREFIX = "MFA_SETUP_PHONE:::";
  public static final String PRE_VERIFICATION_PREFIX = "PRE_VERIFICATION:::";
  public static final String PRE_AUTHENTICATION_PREFIX = "PRE_AUTHENTICATION:::";
  public static final String AUTH_HEADER_KEY = "Authorization";
  public static final String AUTH_HEADER_PREFIX = "Bearer";
  public static final String TOKEN_TYPE_KEY = "tokenType";
  public static final long ACCESS_TOKEN_VALIDITY = getTimeInMillis(60, 60, 5, 0);
  public static final long REFRESH_TOKEN_VALIDITY = getTimeInMillis(60, 60, 24, 2);
  public static final long RESET_PASSWORD_TOKEN_VALIDITY = getTimeInMillis(60, 15, 0, 0);
  public static final String CLAIMS_USER_ID_KEY = "userId";
  public static final String CLAIMS_AUTHORITY_KEY = "authorities";
  public static final String UPDATE_EMAIL_CACHE_PREFIX = "UPDATE_EMAIL:::";
  public static final String UPDATE_PHONE_NUMBER_CACHE_PREFIX = "UPDATE_PHONE_NUMBER:::";
  public static final String PROFILE_VERIFICATION_MESSAGE_TEMPLATE_CACHE_PREFIX = "PROFILE_VERIFICATION_MESSAGE_TEMPLATE:::";
  public static final String COUNTRY_CACHE_PREFIX = "COUNTRY:::";
}
