package com.umulam.fleen.health.adapter.paystack;

import com.umulam.fleen.health.adapter.base.BaseAdapter;
import com.umulam.fleen.health.adapter.paystack.config.PaystackConfig;
import com.umulam.fleen.health.adapter.paystack.model.enums.PaystackParameter;
import com.umulam.fleen.health.adapter.paystack.model.request.ResolveBankAccountRequest;
import com.umulam.fleen.health.adapter.paystack.response.ResolveBankAccountResponse;
import com.umulam.fleen.health.constant.authentication.PaystackType;
import com.umulam.fleen.health.exception.base.FleenHealthException;
import com.umulam.fleen.health.exception.externalsystem.ExternalSystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.HashMap;

import static com.umulam.fleen.health.adapter.paystack.model.enums.PaystackEndpointBlock.BANK;
import static com.umulam.fleen.health.adapter.paystack.model.enums.PaystackEndpointBlock.RESOLVE;

@Slf4j
@Component
public class PaystackAdapter extends BaseAdapter {

  private final PaystackConfig config;

  protected PaystackAdapter(@Value("${paystack.base-url}") String baseUrl,
                            PaystackConfig config) {
    super(baseUrl);
    this.config = config;
  }

  public ResolveBankAccountResponse resolveBankAccount(ResolveBankAccountRequest request) {
    HashMap<String, String> requestBody = new HashMap<>();
    requestBody.put(PaystackParameter.ACCOUNT_NUMBER.getValue(), request.getAccountNumber());
    requestBody.put(PaystackParameter.BANK_CODE.getValue(), request.getBankCode());

    URI uri = buildUri(RESOLVE, BANK);
    ResponseEntity<ResolveBankAccountResponse> response = doCall(uri, HttpMethod.GET,
      getAuthHeaderWithBearerToken(config.getSecretKey()), requestBody, ResolveBankAccountResponse.class);

    if (response.getStatusCode().is2xxSuccessful()) {
      return response.getBody();
    } else {
      String message = String.format("An error occurred while calling resolveBankAccount method of PaystackAdapter: %s", response.getBody());
      log.error(message);
      if (response.getStatusCode().is4xxClientError()) {
        throw new ExternalSystemException(PaystackType.PAYSTACK.getValue());
      } else {
        throw new FleenHealthException(PaystackType.PAYSTACK.getValue());
      }
    }
  }
}
