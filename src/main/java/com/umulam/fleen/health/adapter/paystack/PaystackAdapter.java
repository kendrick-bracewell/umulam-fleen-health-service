package com.umulam.fleen.health.adapter.paystack;

import com.umulam.fleen.health.adapter.ApiParameter;
import com.umulam.fleen.health.adapter.base.BaseAdapter;
import com.umulam.fleen.health.adapter.paystack.config.PaystackConfig;
import com.umulam.fleen.health.adapter.paystack.model.enums.PaystackParameter;
import com.umulam.fleen.health.adapter.paystack.model.request.ResolveBankAccountRequest;
import com.umulam.fleen.health.adapter.paystack.response.GetBanksResponse;
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
    if (isMandatoryFieldAvailable(request.getAccountNumber(), request.getBankCode())) {
      throw new ExternalSystemException(PaystackType.PAYSTACK.getValue());
    }

    HashMap<ApiParameter, String> parameters = new HashMap<>();
    parameters.put(PaystackParameter.ACCOUNT_NUMBER, request.getAccountNumber());
    parameters.put(PaystackParameter.BANK_CODE, request.getBankCode());

    URI uri = buildUri(parameters, RESOLVE, BANK);
    ResponseEntity<ResolveBankAccountResponse> response = doCall(uri, HttpMethod.GET,
      getAuthHeaderWithBearerToken(config.getSecretKey()), null, ResolveBankAccountResponse.class);

    if (response.getStatusCode().is2xxSuccessful()) {
      return response.getBody();
    } else {
      String message = String.format("An error occurred while calling resolveBankAccount method of PaystackAdapter: %s", response.getBody());
      log.error(message);
      handleResponseError(response);
      return null;
    }
  }

  public GetBanksResponse getBanks() {
    URI uri = buildUri(BANK);
    ResponseEntity<GetBanksResponse> response = doCall(uri, HttpMethod.GET,
      getAuthHeaderWithBearerToken(config.getSecretKey()), null, GetBanksResponse.class);

    if (response.getStatusCode().is2xxSuccessful()) {
      return response.getBody();
    } else {
      String message = String.format("An error occurred while calling getBanks method of PaystackAdapter: %s", response.getBody());
      log.error(message);
      handleResponseError(response);
      return null;
    }
  }

  private void handleResponseError(ResponseEntity<?> response) {
    if (response.getStatusCode().is4xxClientError()) {
      throw new ExternalSystemException(PaystackType.PAYSTACK.getValue());
    } else {
      throw new FleenHealthException(PaystackType.PAYSTACK.getValue());
    }
  }
}
