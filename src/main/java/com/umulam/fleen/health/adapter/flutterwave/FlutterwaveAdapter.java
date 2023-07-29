package com.umulam.fleen.health.adapter.flutterwave;

import com.umulam.fleen.health.adapter.base.BaseAdapter;
import com.umulam.fleen.health.adapter.flutterwave.config.FlutterwaveConfig;
import com.umulam.fleen.health.adapter.flutterwave.response.FwGetBanksResponse;
import com.umulam.fleen.health.constant.authentication.PaystackType;
import com.umulam.fleen.health.exception.externalsystem.ExternalSystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;

import static com.umulam.fleen.health.adapter.flutterwave.model.enums.FlutterwaveEndpointBlock.BANKS;

@Slf4j
@Component
public class FlutterwaveAdapter extends BaseAdapter {

  private final FlutterwaveConfig config;

  public FlutterwaveAdapter(@Value("${flutterwave.base-url}") String baseUrl,
                         FlutterwaveConfig config) {
    super(baseUrl);
    this.config = config;
  }

  public FwGetBanksResponse getBanks(String country) {
    if (!isMandatoryFieldAvailable(country)) {
      throw new ExternalSystemException(PaystackType.PAYSTACK.getValue());
    }

    URI uri = buildUri(BANKS, buildPathVar(country.toUpperCase()));
    ResponseEntity<FwGetBanksResponse> response = doCall(uri, HttpMethod.GET,
      getAuthHeaderWithBearerToken(config.getSecretKey()), null, FwGetBanksResponse.class);
    if (response.getStatusCode().is2xxSuccessful()) {
      return response.getBody();
    } else {
      String message = String.format("An error occurred while calling getBanks method of %s: %s", getClass().getSimpleName(), response.getBody());
      log.error(message);
      handleResponseError(response);
      return null;
    }
  }
}
