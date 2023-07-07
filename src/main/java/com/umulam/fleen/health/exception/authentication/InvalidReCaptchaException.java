package com.umulam.fleen.health.exception.authentication;

import com.umulam.fleen.health.exception.base.FleenHealthException;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>The InvalidReCaptchaException class is used to define the exception response message when an invalid captcha token is submitted.
 * </p>
 *
 * @author Yusuf Alamu Musa
 * @version 1.0
 */
public class InvalidReCaptchaException extends FleenHealthException {

  private static final long serialVersionUID = 1L;
  private static final String DEFAULT_MESSAGE = "Invalid ReCaptcha!";

  public InvalidReCaptchaException(String message) {
    super(StringUtils.isEmpty(message) ? DEFAULT_MESSAGE : message);
  }
}
