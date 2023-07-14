package com.umulam.fleen.health.resolver.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umulam.fleen.health.model.request.search.ProfessionalSearchRequest;
import com.umulam.fleen.health.model.request.search.base.SearchRequest;
import com.umulam.fleen.health.resolver.SearchParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class SearchParamArgResolver implements HandlerMethodArgumentResolver {

  private final ObjectMapper mapper;

  public SearchParamArgResolver(ObjectMapper mapper) {
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
    final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getFullURL(request));
    Map<String, String> queryMap = toQueryMap(builder.build().getQueryParams());
    try {
      for (var param : queryMap.entrySet()) {
        System.out.println(param.getKey() + " is the param and the value is " + param.getValue());
      }
      var value = mapper.convertValue(queryMap, parameter.getParameterType());
      System.out.println("The availability status is : " + ((ProfessionalSearchRequest) value).getAvailabilityStatus());
      ((SearchRequest) value).toPageable();
      return value;
//      return value;
    } catch (ClassCastException ex) {
      log.error(ex.getMessage(), ex);
      throw new IllegalArgumentException(ex.getMessage());
    }
  }

  private String getFullURL(HttpServletRequest request) {
    StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
    String queryString = request.getQueryString();

    if (queryString == null) {
      return requestURL.toString();
    } else {
      return requestURL.append('?').append(queryString).toString();
    }
  }

  public Map<String, String> toQueryMap(MultiValueMap<String, String> queryParams) {
    Map<String, String> queryMap = new HashMap<>();
    for (String key : queryParams.keySet()) {
      queryMap.put(key, queryParams.getFirst(key));
    }
    return queryMap;
  }
}
