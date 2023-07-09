package com.umulam.fleen.health.validator.impl;

import com.umulam.fleen.health.validator.FieldMatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;


@Slf4j
@Component
public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

  private String firstFieldName;
  private String secondFieldName;
  private String message;

  @Override
  public void initialize(final FieldMatch constraintAnnotation) {
    firstFieldName = constraintAnnotation.first();
    secondFieldName = constraintAnnotation.second();
    message = constraintAnnotation.message();
  }

  @Override
  public boolean isValid(final Object object, final ConstraintValidatorContext context) {
    boolean valid = true;
    try
    {
      final Object firstObj = getFieldValue(object, firstFieldName);
      final Object secondObj = getFieldValue(object, secondFieldName);
      valid =  firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj);
    }
    catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }

    if (!valid) {
      context.buildConstraintViolationWithTemplate(message)
              .addPropertyNode(firstFieldName)
              .addConstraintViolation()
              .disableDefaultConstraintViolation();
    }
    return valid;
  }

  private Object getFieldValue(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
    Class<?> clazz = object.getClass();
    try {
      Field field = clazz.getDeclaredField(fieldName);
      ReflectionUtils.makeAccessible(field);
      return field.get(object);
    } catch (NoSuchFieldException e) {
      throw new NoSuchFieldException("Field '" + fieldName + "' does not exist on object of class " + clazz.getName());
    } catch (IllegalAccessException e) {
      throw new IllegalAccessException("Cannot access field '" + fieldName + "' on object of class " + clazz.getName());
    }
  }
}
