package com.umulam.fleen.health.adapter.flutterwave;

import com.umulam.fleen.health.adapter.ApiParameter;
import com.umulam.fleen.health.adapter.base.BaseAdapter;
import com.umulam.fleen.health.adapter.flutterwave.config.FlutterwaveConfig;
import com.umulam.fleen.health.adapter.flutterwave.model.request.*;
import com.umulam.fleen.health.adapter.flutterwave.model.response.*;
import com.umulam.fleen.health.aspect.RetryOnFailure;
import com.umulam.fleen.health.constant.authentication.PaymentGatewayType;
import com.umulam.fleen.health.exception.externalsystem.ExternalSystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.HashMap;

import static com.umulam.fleen.health.adapter.flutterwave.model.enums.FlutterwaveEndpointBlock.*;
import static com.umulam.fleen.health.adapter.flutterwave.model.enums.FlutterwaveParameter.*;

@Slf4j
@Component
public class FlutterwaveAdapter extends BaseAdapter {

  private final FlutterwaveConfig config;

  public FlutterwaveAdapter(@Value("${flutterwave.base-url}") String baseUrl,
                         FlutterwaveConfig config) {
    super(baseUrl);
    this.config = config;
  }

  public FwResolveBankAccountResponse resolveBankAccount(FwResolveBankAccountRequest request) {
    if (!isMandatoryFieldAvailable(request.getAccountNumber(), request.getBankCode())) {
      throw new ExternalSystemException(PaymentGatewayType.FLUTTERWAVE.getValue());
    }

    URI uri = buildUri(ACCOUNTS, RESOLVE);
    ResponseEntity<FwResolveBankAccountResponse> response = doCall(uri, HttpMethod.POST,
      getAuthHeaderWithBearerToken(config.getSecretKey()), request, FwResolveBankAccountResponse.class);

    if (response.getStatusCode().is2xxSuccessful()) {
      return response.getBody();
    } else {
      String message = String.format("An error occurred while calling resolveBankAccount method of %s: %s", getClass().getSimpleName(), response.getBody());
      log.error(message);
      handleResponseError(response);
      return null;
    }
  }

  public FwGetBanksResponse getBanks(String country) {
    if (!isMandatoryFieldAvailable(country)) {
      throw new ExternalSystemException(PaymentGatewayType.FLUTTERWAVE.getValue());
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

  public FwVerifyTransactionResponse verifyTransactionById(String transactionId) {
    if (!isMandatoryFieldAvailable(transactionId)) {
      throw new ExternalSystemException(PaymentGatewayType.FLUTTERWAVE.getValue());
    }

    URI uri = buildUri(TRANSACTIONS, buildPathVar(transactionId), VERIFY);
    ResponseEntity<FwVerifyTransactionResponse> response = doCall(uri, HttpMethod.GET,
      getAuthHeaderWithBearerToken(config.getSecretKey()), null, FwVerifyTransactionResponse.class);

    if (response.getStatusCode().is2xxSuccessful()) {
      return response.getBody();
    } else {
      String message = String.format("An error occurred while calling verifyTransactionById method of %s: %s", getClass().getSimpleName(), response.getBody());
      log.error(message);
      handleResponseError(response);
      return null;
    }
  }

  public FwVerifyTransactionResponse verifyTransactionByReference(String transactionReference) {
    if (!isMandatoryFieldAvailable(transactionReference)) {
      throw new ExternalSystemException(PaymentGatewayType.FLUTTERWAVE.getValue());
    }

    HashMap<ApiParameter, String> parameters = new HashMap<>();
    parameters.put(TRANSACTION_REFERENCE, transactionReference);

    URI uri = buildUri(parameters, TRANSACTIONS, VERIFY_BY_REFERENCE);
    ResponseEntity<FwVerifyTransactionResponse> response = doCall(uri, HttpMethod.GET,
      getAuthHeaderWithBearerToken(config.getSecretKey()), null, FwVerifyTransactionResponse.class);

    if (response.getStatusCode().is2xxSuccessful()) {
      return response.getBody();
    } else {
      String message = String.format("An error occurred while calling verifyTransactionByReference method of %s: %s", getClass().getSimpleName(), response.getBody());
      log.error(message);
      handleResponseError(response);
      return null;
    }
  }

  public FwCreateRefundResponse createRefund(FwCreateRefundRequest request) {
    if (!isMandatoryFieldAvailable(request.getTransactionId())) {
      throw new ExternalSystemException(PaymentGatewayType.FLUTTERWAVE.getValue());
    }

    URI uri = buildUri(TRANSACTIONS, buildPathVar(request.getTransactionId()), REFUND);
    ResponseEntity<FwCreateRefundResponse> response = doCall(uri, HttpMethod.POST,
      getAuthHeaderWithBearerToken(config.getSecretKey()), request, FwCreateRefundResponse.class);

    if (response.getStatusCode().is2xxSuccessful()) {
      return response.getBody();
    } else {
      String message = String.format("An error occurred while calling createRefund method of %s: %s", getClass().getSimpleName(), response.getBody());
      log.error(message);
      handleResponseError(response);
      return null;
    }
  }

  @RetryOnFailure
  public FwGetExchangeRateResponse getExchangeRate(FwGetExchangeRateRequest request) {
    if (!isMandatoryFieldAvailable(request.getAmount(), request.getSourceCurrency(), request.getDestinationCurrency())) {
      throw new ExternalSystemException(PaymentGatewayType.FLUTTERWAVE.getValue());
    }

    HashMap<ApiParameter, String> parameters = new HashMap<>();
    parameters.put(AMOUNT, request.getAmount());
    parameters.put(SOURCE_CURRENCY, request.getSourceCurrency());
    parameters.put(DESTINATION_CURRENCY, request.getDestinationCurrency());

    URI uri = buildUri(parameters, TRANSFERS, RATES);
    ResponseEntity<FwGetExchangeRateResponse> response = doCall(uri, HttpMethod.GET,
      getAuthHeaderWithBearerToken(config.getSecretKey()), null, FwGetExchangeRateResponse.class);

    if (response.getStatusCode().is2xxSuccessful()) {
      return response.getBody();
    } else {
      String message = String.format("An error occurred while calling getExchangeRate method of %s: %s", getClass().getSimpleName(), response.getBody());
      log.error(message);
      handleResponseError(response);
      return null;
    }
  }

  @RetryOnFailure
  public FwGetExchangeRateResponse createTransfer(FwCreateTransferRequest request) {
    if (!isMandatoryFieldAvailable(request.getDestinationAmount(), request.getSourceCurrency(), request.getDestinationCurrency())) {
      throw new ExternalSystemException(PaymentGatewayType.FLUTTERWAVE.getValue());
    }

    URI uri = buildUri(TRANSFERS);
    ResponseEntity<FwGetExchangeRateResponse> response = doCall(uri, HttpMethod.POST,
      getAuthHeaderWithBearerToken(config.getSecretKey()), request, FwGetExchangeRateResponse.class);

    if (response.getStatusCode().is2xxSuccessful()) {
      return response.getBody();
    } else {
      String message = String.format("An error occurred while calling createTransfer method of %s: %s", getClass().getSimpleName(), response.getBody());
      log.error(message);
      handleResponseError(response);
      return null;
    }
  }

  @RetryOnFailure
  public FwRetryTransferResponse retryTransfer(String transferId) {
    if (!isMandatoryFieldAvailable(transferId)) {
      throw new ExternalSystemException(PaymentGatewayType.FLUTTERWAVE.getValue());
    }

    URI uri = buildUri(TRANSFERS, buildPathVar(transferId), RETRIES);
    ResponseEntity<FwRetryTransferResponse> response = doCall(uri, HttpMethod.POST,
      getAuthHeaderWithBearerToken(config.getSecretKey()), null, FwRetryTransferResponse.class);

    if (response.getStatusCode().is2xxSuccessful()) {
      return response.getBody();
    } else {
      String message = String.format("An error occurred while calling retryTransfer method of %s: %s", getClass().getSimpleName(), response.getBody());
      log.error(message);
      handleResponseError(response);
      return null;
    }
  }

  public FwGetBankBranchesResponse getBankBranches(String bankId) {
    if (!isMandatoryFieldAvailable(bankId)) {
      throw new ExternalSystemException(PaymentGatewayType.FLUTTERWAVE.getValue());
    }

    URI uri = buildUri(BANKS, buildPathVar(bankId), BRANCHES);
    ResponseEntity<FwGetBankBranchesResponse> response = doCall(uri, HttpMethod.GET,
      getAuthHeaderWithBearerToken(config.getSecretKey()), null, FwGetBankBranchesResponse.class);
    if (response.getStatusCode().is2xxSuccessful()) {
      return response.getBody();
    } else {
      String message = String.format("An error occurred while calling getBankBranches method of %s: %s", getClass().getSimpleName(), response.getBody());
      log.error(message);
      handleResponseError(response);
      return null;
    }
  }

  @RetryOnFailure
  public FxGetTransferFeeResponse getTransferFee(FxGetTransferFeeRequest request) {
    if (!isMandatoryFieldAvailable(request.getAmount(), request.getCurrency())) {
      throw new ExternalSystemException(PaymentGatewayType.FLUTTERWAVE.getValue());
    }

    HashMap<ApiParameter, String> parameters = new HashMap<>();
    parameters.put(AMOUNT, request.getAmount());
    parameters.put(CURRENCY, request.getCurrency());

    URI uri = buildUri(parameters, TRANSFERS, FEE);
    ResponseEntity<FxGetTransferFeeResponse> response = doCall(uri, HttpMethod.GET,
      getAuthHeaderWithBearerToken(config.getSecretKey()), null, FxGetTransferFeeResponse.class);

    if (response.getStatusCode().is2xxSuccessful()) {
      return response.getBody();
    } else {
      String message = String.format("An error occurred while calling getTransferFee method of %s: %s", getClass().getSimpleName(), response.getBody());
      log.error(message);
      handleResponseError(response);
      return null;
    }
  }
}
