package com.umulam.fleen.health.exception.handler;

import com.umulam.fleen.health.exception.authentication.*;
import com.umulam.fleen.health.exception.country.CountryCodeDuplicateException;
import com.umulam.fleen.health.exception.country.CountryDuplicateException;
import com.umulam.fleen.health.exception.country.CountryNotFoundException;
import com.umulam.fleen.health.exception.memberstatus.MemberStatusCodeDuplicateException;
import com.umulam.fleen.health.exception.memberstatus.MemberStatusNotFoundException;
import com.umulam.fleen.health.exception.role.RoleDuplicateException;
import com.umulam.fleen.health.exception.role.RoleNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.umulam.fleen.health.constant.base.ExceptionConstant.*;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class FleenHealthExceptionHandler {

  @ResponseStatus(value = NOT_FOUND)
  @ExceptionHandler(value = {
          CountryNotFoundException.class,
          RoleNotFoundException.class,
          MemberStatusNotFoundException.class
  })
  public Object handleNotFound(Exception ex) {
    log.error(ex.getMessage(), ex);
    return buildErrorMap(ex.getMessage(), NOT_FOUND);
  }

  @ResponseStatus(value = CONFLICT)
  @ExceptionHandler(value = {
          CountryDuplicateException.class,
          RoleDuplicateException.class,
          CountryCodeDuplicateException.class,
          MemberStatusCodeDuplicateException.class,
  })
  public Object handleDuplicate(Exception ex) {
    log.error(ex.getMessage(), ex);
    return buildErrorMap(ex.getMessage(), CONFLICT);
  }

  @ResponseStatus(value = UNAUTHORIZED)
  @ExceptionHandler(value = {
          InvalidAuthenticationException.class
  })
  public Object handleUnauthorized(Exception ex) {
    log.error(ex.getMessage(), ex);
    return buildErrorMap(ex.getMessage(), UNAUTHORIZED);
  }

  @ResponseStatus(value = UNAUTHORIZED)
  @ExceptionHandler(value = {
          ExpiredJwtException.class,
          MalformedJwtException.class,
          SignatureException.class
  })
  public Object handleUnauthorized() {
    return buildErrorMap(INVALID_USER, UNAUTHORIZED);
  }

  @ResponseStatus(value = FORBIDDEN)
  @ExceptionHandler(value = {
          AccessDeniedException.class
  })
  public Object forbidden(AccessDeniedException ex, HttpServletRequest request) {
    log.error(ex.getMessage(), ex);
    var map = buildErrorMap(FORBIDDEN_ACCESS, FORBIDDEN);
    map.put(PATH_URL, request.getServletPath());
    return map;
  }

  @ResponseStatus(value = BAD_REQUEST)
  @ExceptionHandler(value = {
          VerificationFailedException.class,
          ExpiredVerificationCodeException.class,
          InvalidVerificationCodeException.class,
          MfaGenerationFailedException.class,
          AlreadySignedUpException.class,
          DisabledAccountException.class
  })
  public Object handleInvalid(Exception ex) {
    log.error(ex.getMessage(), ex);
    return buildErrorMap(ex.getMessage(), BAD_REQUEST);
  }

  @ResponseStatus(value = BAD_REQUEST)
  @ExceptionHandler(value = { HttpMediaTypeNotSupportedException.class })
  public Object handleUnsupportedMediaType() {
    return buildErrorMap(UNSUPPORTED_CONTENT_TYPE, BAD_REQUEST);
  }

  @ResponseStatus(value = BAD_REQUEST)
  @ExceptionHandler(value = { HttpRequestMethodNotSupportedException.class })
  public Object handleUnsupportedRequestMethod() {
    return buildErrorMap(UNSUPPORTED_HTTP_REQUEST_METHOD, BAD_REQUEST);
  }

  @ResponseStatus(value = BAD_REQUEST)
  @ExceptionHandler(value = { HttpMessageNotReadableException.class })
  public Object handleNotReadable() {
    return buildErrorMap(INVALID_REQUEST_BODY, BAD_REQUEST);
  }

  @ResponseStatus(value = INTERNAL_SERVER_ERROR)
  @ExceptionHandler(value = { Exception.class })
  public Object handleException(Exception ex) {
    return buildErrorMap(ex.getMessage(), INTERNAL_SERVER_ERROR);
  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Object handleDataValidationError(MethodArgumentNotValidException ex) {
    List<Map<String, Object>> values = new ArrayList<>();
    ex
      .getBindingResult()
      .getFieldErrors()
      .parallelStream()
      .map(FieldError::getField)
      .distinct()
      .forEach(field -> {
        Map<String, Object> detail = new HashMap<>();
        List<String> errors = ex
                .getFieldErrors(field)
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        detail.put("field_name", LOWER_CAMEL.to(LOWER_UNDERSCORE, field));
        detail.put("errors", errors);
        values.add(detail);
      });


    Map<String, Object> error = new HashMap<>(buildErrorMap(INVALID_REQUEST_BODY, BAD_REQUEST));
    error.put("fields", values);
    error.put("type", "DataValidation");
    return error;
  }

  private Map<String, Object> buildErrorMap(String message, HttpStatus status) {
    Map<String, Object> error = new HashMap<>();
    error.put("message", message);
    error.put("status", status.value());
    error.put("timestamp", LocalDateTime.now().toString());
    return error;
  }
}
