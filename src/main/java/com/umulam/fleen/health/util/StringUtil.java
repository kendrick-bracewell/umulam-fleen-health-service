package com.umulam.fleen.health.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * The StringUtils contains implementations and methods for converting a number represented as a string to various type of numbers
 * for example an integer or whole number.
 *
 * @author Alperen Oezkan
 * @author Shady Hussein
 */
@Slf4j
public class StringUtil {

  private StringUtil() {
  }

  public static Long toLong(String strValue) {
    try {
      return Long.valueOf(strValue);
    } catch (NumberFormatException | NullPointerException e) {
      log.error(
          String.format("An error occurred while trying to convert string='%s' to a Long: %s",
              strValue,
              e.getMessage()));
      return null;
    }
  }

  public static Integer toInteger(String strValue) {
    try {
      return Integer.valueOf(strValue);
    } catch (NumberFormatException | NullPointerException e) {
      log.error(
          String.format("An error occurred while trying to convert string='%s' to a Integer: %s",
              strValue,
              e.getMessage()));
      return null;
    }
  }

  public static Float toFloat(String strValue) {
    try {
      return Float.valueOf(strValue);
    } catch (NumberFormatException | NullPointerException e) {
      log.error(
          String.format("An error occurred while trying to convert string='%s' to a Float: %s",
              strValue,
              e.getMessage()));
      return null;
    }
  }

  public static String payloadAsJsonString(final Object payload) {
    try {
      return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(payload);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String getFullName(String firstName, String lastName) {
    return getFullName(firstName, lastName, null);
  }

  public static String getFullName(String firstName, String lastName, String middleName) {
    return
      Objects.toString(firstName, "") + " "
      + Objects.toString(lastName, "") + " "
      + Objects.toString(middleName, "");
  }
}
