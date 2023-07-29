package com.umulam.fleen.health.adapter.paystack;

import com.umulam.fleen.health.adapter.ApiParameter;
import com.umulam.fleen.health.adapter.base.BaseAdapter;
import com.umulam.fleen.health.adapter.paystack.config.PaystackConfig;
import com.umulam.fleen.health.adapter.paystack.model.enums.PaystackParameter;
import com.umulam.fleen.health.adapter.paystack.model.request.CreateTransferRecipientRequest;
import com.umulam.fleen.health.adapter.paystack.model.request.InitiateTransferRequest;
import com.umulam.fleen.health.adapter.paystack.model.request.ResolveBankAccountRequest;
import com.umulam.fleen.health.adapter.paystack.response.*;
import com.umulam.fleen.health.constant.authentication.PaystackType;
import com.umulam.fleen.health.exception.externalsystem.ExternalSystemException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.HashMap;

import static com.umulam.fleen.health.adapter.paystack.model.enums.PaystackEndpointBlock.*;

@Slf4j
@Component
@Setter
public class PaystackAdapter extends BaseAdapter {

  private final PaystackConfig config;

  public PaystackAdapter(@Value("${paystack.base-url}") String baseUrl,
                            PaystackConfig config) {
    super(baseUrl);
    this.config = config;
  }

  public ResolveBankAccountResponse resolveBankAccount(ResolveBankAccountRequest request) {
    if (!isMandatoryFieldAvailable(request.getAccountNumber(), request.getBankCode())) {
      throw new ExternalSystemException(PaystackType.PAYSTACK.getValue());
    }

    HashMap<ApiParameter, String> parameters = new HashMap<>();
    parameters.put(PaystackParameter.ACCOUNT_NUMBER, request.getAccountNumber());
    parameters.put(PaystackParameter.BANK_CODE, request.getBankCode());

    URI uri = buildUri(parameters, BANK, RESOLVE);
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

  public GetBanksResponse getBanks(String currency) {
    if (!isMandatoryFieldAvailable(currency)) {
      throw new ExternalSystemException(PaystackType.PAYSTACK.getValue());
    }

    HashMap<ApiParameter, String> parameters = new HashMap<>();
    parameters.put(PaystackParameter.CURRENCY, currency.toUpperCase());

    URI uri = buildUri(parameters, BANK);
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

  public CreateTransferRecipientResponse createTransferRecipient(CreateTransferRecipientRequest request) {
    if (!isMandatoryFieldAvailable(request.getAccountNumber(), request.getBankCode(), request.getType(), request.getAccountName())) {
      throw new ExternalSystemException(PaystackType.PAYSTACK.getValue());
    }

    URI uri = buildUri(TRANSFER_RECIPIENT);
    ResponseEntity<CreateTransferRecipientResponse> response = doCall(uri, HttpMethod.POST,
      getAuthHeaderWithBearerToken(config.getSecretKey()), request, CreateTransferRecipientResponse.class);

    if (response.getStatusCode().is2xxSuccessful()) {
      return response.getBody();
    } else {
      String message = String.format("An error occurred while calling createTransferRecipient method of PaystackAdapter: %s", response.getBody());
      log.error(message);
      handleResponseError(response);
      return null;
    }
  }

  public InitiateTransferResponse initiateTransfer(InitiateTransferRequest request) {
    if (!isMandatoryFieldAvailable(request.getRecipient(), request.getAmount(), request.getReference(), request.getSource())) {
      throw new ExternalSystemException(PaystackType.PAYSTACK.getValue());
    }

    URI uri = buildUri(TRANSFER);
    ResponseEntity<InitiateTransferResponse> response = doCall(uri, HttpMethod.POST,
      getAuthHeaderWithBearerToken(config.getSecretKey()), request, InitiateTransferResponse.class);

    if (response.getStatusCode().is2xxSuccessful()) {
      return response.getBody();
    } else {
      String message = String.format("An error occurred while calling initiateTransfer method of PaystackAdapter: %s", response.getBody());
      log.error(message);
      handleResponseError(response);
      return null;
    }
  }

  public void deleteTransferRecipient(String recipientCode) {
    if (!isMandatoryFieldAvailable(recipientCode)) {
      throw new ExternalSystemException(PaystackType.PAYSTACK.getValue());
    }

    URI uri = buildUri(TRANSFER_RECIPIENT);
    ResponseEntity<DeleteTransferRecipientResponse> response = doCall(uri, HttpMethod.DELETE,
      getAuthHeaderWithBearerToken(config.getSecretKey()), null, DeleteTransferRecipientResponse.class);

    if (!response.getStatusCode().is2xxSuccessful()) {
      String message = String.format("An error occurred while calling deleteTransferRecipient method of PaystackAdapter: %s", response.getBody());
      log.error(message);
      handleResponseError(response);
    }
    return;
  }
}
