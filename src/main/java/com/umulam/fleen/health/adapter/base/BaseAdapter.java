package com.umulam.fleen.health.adapter.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umulam.fleen.health.adapter.ApiParameter;
import com.umulam.fleen.health.adapter.EndpointBlock;
import com.umulam.fleen.health.constant.authentication.PaymentGatewayType;
import com.umulam.fleen.health.exception.base.FleenHealthException;
import com.umulam.fleen.health.exception.externalsystem.ExternalSystemException;
import com.umulam.fleen.health.util.AuthUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Setter
public class BaseAdapter {


  @NotBlank
  protected String baseUrl;
  protected RestTemplate restTemplate;

  protected BaseAdapter(String baseUrl) {
    this.baseUrl = baseUrl;
    this.restTemplate = new RestTemplateBuilder().requestFactory(
              SimpleClientHttpRequestFactory::new).build();
  }

  public <T> ResponseEntity<T> doCall(@NonNull URI uri, @NonNull HttpMethod method,
                                      @Nullable Map<String, String> headers,
                                      @Nullable Object body, @NonNull Class<T> responseModel) {
    log.info(String.format("HTTP call to url=%s with method=%s and body=%s", uri, method.name(),
            getPayloadBodyAsString(body)));
    HttpHeaders requestHeaders = getHeaders();
    if (headers != null) {
      headers.forEach(requestHeaders::add);
    }
    try {
      return restTemplate.exchange(uri, method, new HttpEntity<>(body, requestHeaders),
              responseModel);
    } catch (HttpStatusCodeException e) {
      log.error(String.format(
              "An error occurred while HTTP call to url=%s with method=%s and body=%s: %s", uri,
              method.name(),
              getPayloadBodyAsString(body), e.getMessage()));
      return (ResponseEntity<T>) ResponseEntity.status(e.getRawStatusCode())
              .headers(e.getResponseHeaders()).body(e.getResponseBodyAsString());
    }
  }

  public String getPayloadBodyAsString(Object body) {
    String payloadAsString = "";
    if (body instanceof String) {
      payloadAsString = (String) body;
    } else {
      try {
        payloadAsString = new ObjectMapper().writeValueAsString(body);
      } catch (JsonProcessingException e1) {
      }
    }
    return payloadAsString;
  }

  protected UriComponentsBuilder initUriBuilder(EndpointBlock... urlBlocks) {
    StringBuilder urlBuilder = new StringBuilder(baseUrl);
    for (EndpointBlock block : urlBlocks) {
      if (block != null) {
        urlBuilder.append(block.getValue());
      }
    }
    return UriComponentsBuilder.fromHttpUrl(urlBuilder.toString());
  }

  protected HttpHeaders getHeaders() {
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_JSON);
    return requestHeaders;
  }

  protected URI buildUri(String url) {
    return UriComponentsBuilder.fromHttpUrl(url).build().toUri();
  }

  protected URI buildUri(EndpointBlock... urlBlocks) {
    return initUriBuilder(urlBlocks).build().toUri();
  }

  protected URI buildUri(Map<ApiParameter, String> queryParams, EndpointBlock... urlBlocks) {
    UriComponentsBuilder uriComponentsBuilder = initUriBuilder(urlBlocks);
    queryParams.forEach((k, v) -> uriComponentsBuilder.queryParam(k.getValue(), v));
    return uriComponentsBuilder.build().toUri();
  }

  protected EndpointBlock buildPathVar(Object object) {
    return new BaseEndpointBlock("/" + object);
  }

  protected Map<String, String> getAuthHeaderWithBasicAuth(String username, String token) {
    return getAuthHeader(AuthUtil.getBasicAuthToken(username, token));
  }

  protected Map<String, String> getAuthHeaderWithBearerToken(String token) {
    return getAuthHeader(AuthUtil.getBearerToken(token));
  }

  protected Map<String, String> getAuthHeader(String value) {
    Map<String, String> headers = new HashMap<>();
    headers.put(HttpHeaders.AUTHORIZATION, value);
    return headers;
  }

  public Map<ApiParameter, String> addParameterIfTrue(Map<ApiParameter, String> queryParams, ApiParameter key,
                                                      String value,
                                                      boolean condition) {
    if (condition) {
      queryParams.put(key, value);
    }
    return queryParams;
  }

  public Map<ApiParameter, String> addParameterIfTrue(Map<ApiParameter, String> queryParams, ApiParameter key,
                                                      Object value,
                                                      boolean condition) {
    if (condition) {
      queryParams.put(key, value.toString());
    }
    return queryParams;
  }

  public Map<ApiParameter, String> addParameterIfNotBlank(Map<ApiParameter, String> queryParams, ApiParameter key,
                                                          String value) {
    addParameterIfTrue(queryParams, key, value, value != null && !value.isBlank());
    return queryParams;
  }

  public Map<ApiParameter, String> addParameterIfNotBlank(Map<ApiParameter, String> queryParams, ApiParameter key,
                                                          Object value) {
    addParameterIfTrue(queryParams, key, value, value != null);
    return queryParams;
  }

  public boolean isMandatoryFieldAvailable(Object... value) {
    for (Object o : value) {
      if (o == null || o.toString().isBlank()) {
        return false;
      }
    }
    return true;
  }

  protected void handleResponseError(ResponseEntity<?> response) {
    if (response.getStatusCode().is4xxClientError()) {
      throw new ExternalSystemException(PaymentGatewayType.PAYSTACK.getValue());
    } else {
      throw new FleenHealthException(PaymentGatewayType.PAYSTACK.getValue());
    }
  }
}
