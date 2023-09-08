package com.umulam.fleen.health.util;

import com.umulam.fleen.health.model.view.base.EnumView;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EnumUtil {

  private static final char ENUM_VALUE_SEPARATOR = '_';
  private static final char ENUM_VALUE_REPLACE = ' ';

  public static List<Long> getValues(Class<?> enumClass) {
    try {
      List<Long> values = new ArrayList<>();
      Method valuesMethod = enumClass.getMethod("values");
      Object[] allEnums = (Object[]) valuesMethod.invoke(null);

      for (Object enumValue : allEnums) {
        Method ordinalMethod = enumClass.getMethod("ordinal");
        Long ordinalValue = (long) ordinalMethod.invoke(enumValue);
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


    public static List<? extends EnumView> convertEnumToList(Class<? extends Enum<?>> enumClass) {
    List<EnumView> views = new ArrayList<>();

    if (enumClass.isEnum()) {
      Enum<?>[] constants = enumClass.getEnumConstants();

      for (Enum<?> enumValue : constants) {
        EnumView enumView = new EnumView();
        enumView.setLabel(enumValue.toString()
          .replaceAll(String.valueOf(ENUM_VALUE_SEPARATOR), String.valueOf(ENUM_VALUE_REPLACE)));
        enumView.setName(enumValue.toString());
        views.add(enumView);
      }
    }

    return views;
  }
}
