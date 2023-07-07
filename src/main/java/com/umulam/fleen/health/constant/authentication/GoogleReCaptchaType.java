package com.umulam.fleen.health.constant.authentication;

import com.umulam.fleen.health.constant.ExternalSystemType;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Yusuf Alamu Musa
 * @version 1.0
 */
@Slf4j
public enum GoogleReCaptchaType implements ExternalSystemType {

  GOOGLE_RECAPTCHA("Google ReCaptcha");

  private final String value;

  GoogleReCaptchaType(String value) {
    this.value = value;
  }

  public static GoogleReCaptchaType toGoogleReCaptcha(String input) {
    try {
      return GoogleReCaptchaType.valueOf(input);
    } catch (IllegalArgumentException e) {
      log.error(String.format("Input=%s is not suitable to cast GoogleReCaptchaType", input));
      return null;
    }
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public String getLevel() {
    return null;
  }
}
