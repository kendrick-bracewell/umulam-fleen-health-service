package com.umulam.fleen.health.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EnumUtil {

  public static List<Integer> getValues(Class<?> enumClass) {
    try {
      List<Integer> values = new ArrayList<>();
      Method valuesMethod = enumClass.getMethod("values");
      Object[] allEnums = (Object[]) valuesMethod.invoke(null);

      for (Object enumValue : allEnums) {
        Method ordinalMethod = enumClass.getMethod("ordinal");
        int ordinalValue = (int) ordinalMethod.invoke(enumValue);
        values.add(ordinalValue);
      }
      return values;
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
      log.error(ex.getMessage(), ex);
    }
    return null;
  }

  public static <T extends Enum<T>> T getEnumConstant(Class<T> enumType, int ordinalValue) {
    T[] allEnums = enumType.getEnumConstants();
    if (ordinalValue >= 0 && ordinalValue < allEnums.length) {
      return allEnums[ordinalValue];
    } else {
      throw new IllegalArgumentException("Invalid ordinal value for the Enum.");
    }
  }

}
