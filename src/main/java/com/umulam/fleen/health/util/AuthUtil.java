package com.umulam.fleen.health.util;

import com.umulam.fleen.health.exception.base.FleenHealthException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.validation.constraints.NotEmpty;

/**
 * The AuthUtils contains implementations and methods for dealing with application and user related security
 */
@Slf4j
public class AuthUtil {

  private static final String BEARER = "Bearer ";
  private static final String BASIC = "Basic ";

  private AuthUtil() {
  }

  /**
   * This method takes in a username and password, returns a base64 string encoded which can be used for Basic authentication
   *
   * @param username the username of the user
   * @param password the password hash of the user
   * @return the base 64 encoded string to be used for Basic authentication
   */
  public static String getBasicAuthToken(@NonNull String username, @NonNull String password) {
    return BASIC + new String(Base64.encodeBase64((username + ":" + password).getBytes()));
  }

  public static String getBearerToken(@NotEmpty String token) {
    return BEARER + token;
  }

  /**
   * This method takes in a token string to check and verify whether it is valid
   *
   * @param token the bearer token
   * @return the actual token without the "Bearer" prefix
   * @throws com.umulam.fleen.health.exception.authentication.InvalidAuthenticationException if the token is not a bearer token
   */
  public static String stripBearerToken(@NotEmpty String token) {
    if (!token.startsWith(BEARER)) {
      String message = "Token is not a bearer token!";
      log.error(message);
      throw new FleenHealthException(message);
    }
    return token.substring(BEARER.length());
  }

  /**
   * Checks if a string is a bearer token or begins with "Bearer" prefix
   *
   * @param token the bearer token
   * @return true or false if the string begins with the "Bearer" prefix
   */
  public static boolean isBearerToken(String token) {
    return token != null && token.startsWith(BEARER);
  }

}
