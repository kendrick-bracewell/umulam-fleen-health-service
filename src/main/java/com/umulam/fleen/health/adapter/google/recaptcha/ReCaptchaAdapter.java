package com.umulam.fleen.health.adapter.google.recaptcha;

import com.umulam.fleen.health.adapter.base.BaseAdapter;
import com.umulam.fleen.health.adapter.google.recaptcha.model.enums.GoogleRecaptchaEndpointBlock;
import com.umulam.fleen.health.adapter.google.recaptcha.model.enums.GoogleRecaptchaParameter;
import com.umulam.fleen.health.adapter.google.recaptcha.model.response.ReCaptchaResponse;
import com.umulam.fleen.health.constant.authentication.GoogleReCaptchaType;
import com.umulam.fleen.health.exception.base.FleenHealthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.validation.constraints.NotBlank;
import java.net.URI;

/**
 * The ReCaptchaAdapter is used for authentication and security purpose to verify that
 * the request being sent to the application is made by a human and not a bot or machine.
 *
 * @author Yusuf Alamu Musa
 */
@Slf4j
@Component
public class ReCaptchaAdapter extends BaseAdapter {

  @NotBlank
  private final String recaptchaSecret;

  protected ReCaptchaAdapter(@Value("${google.recaptcha.base-url}") String baseUrl,
                             @Value("${google.recaptcha.secret-key}") String secretKey) {
    super(baseUrl);
    this.recaptchaSecret = secretKey;
  }

  public ReCaptchaResponse verifyRecaptcha(String reCaptchaToken) {
    MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
    requestBody.add(GoogleRecaptchaParameter.SECRET.getValue(), recaptchaSecret);
    requestBody.add(GoogleRecaptchaParameter.RESPONSE.getValue(), reCaptchaToken);

    URI uri = buildUri(GoogleRecaptchaEndpointBlock.SITE_VERIFY);

    ResponseEntity<ReCaptchaResponse> response = doCall(uri, HttpMethod.POST,
        null, requestBody, ReCaptchaResponse.class);

    if (response.getStatusCode().is2xxSuccessful()) {
      return response.getBody();
    } else {
      log.error(String.format(
            "An error occurred while calling verifyRecaptcha method of RecaptchaAdapter: %s",
            response.getBody()));
      throw new FleenHealthException(GoogleReCaptchaType.GOOGLE_RECAPTCHA.getValue());
    }
  }

    @Override
    protected HttpHeaders getHeaders() {
      HttpHeaders requestHeaders = new HttpHeaders();
      requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
      return requestHeaders;
    }
}
