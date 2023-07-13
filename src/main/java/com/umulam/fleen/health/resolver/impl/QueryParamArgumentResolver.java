package com.umulam.fleen.health.resolver.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umulam.fleen.health.exception.base.FleenHealthException;
import com.umulam.fleen.health.resolver.SearchParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpUtils;
import java.util.Arrays;
import java.util.HashMap;

import static com.umulam.fleen.health.util.FleenHealthUtil.convertQueryStringToHashMap;

@Slf4j
@Component
public class QueryParamArgumentResolver implements HandlerMethodArgumentResolver {

  private final ObjectMapper mapper;

  public QueryParamArgumentResolver(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterAnnotation(SearchParam.class) != null;
  }

  @Override
  public Object resolveArgument(MethodParameter parameter,
                                ModelAndViewContainer mavContainer,
                                NativeWebRequest webRequest,
                                WebDataBinderFactory binderFactory) {
    try {
      final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
      final HashMap<String, String> json = convertQueryStringToHashMap(request.getQueryString());
      return mapper.convertValue(json, parameter.getParameterType());
    } catch (IllegalArgumentException ex) {
      log.error(ex.getMessage(), ex);
    }
    return null;
  }
}
