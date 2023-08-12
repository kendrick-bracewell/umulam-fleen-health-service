package com.umulam.fleen.health.adapter.banking.flutterwave;

import com.umulam.fleen.health.adapter.ApiParameter;
import com.umulam.fleen.health.adapter.banking.flutterwave.config.FlutterwaveConfig;
import com.umulam.fleen.health.adapter.banking.flutterwave.model.enums.FlutterwaveEndpointBlock;
import com.umulam.fleen.health.adapter.banking.flutterwave.model.enums.FlutterwaveParameter;
import com.umulam.fleen.health.adapter.banking.flutterwave.model.request.*;
import com.umulam.fleen.health.adapter.banking.flutterwave.model.response.*;
import com.umulam.fleen.health.adapter.base.BaseAdapter;
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

    URI uri = buildUri(FlutterwaveEndpointBlock.ACCOUNTS, FlutterwaveEndpointBlock.RESOLVE);
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

    URI uri = buildUri(FlutterwaveEndpointBlock.BANKS, buildPathVar(country.toUpperCase()));
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

    URI uri = buildUri(FlutterwaveEndpointBlock.TRANSACTIONS, buildPathVar(transactionId), FlutterwaveEndpointBlock.VERIFY);
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

  @RetryOnFailure
  public FwVerifyTransactionResponse verifyTransactionByReference(String transactionReference) {
    if (!isMandatoryFieldAvailable(transactionReference)) {
      throw new ExternalSystemException(PaymentGatewayType.FLUTTERWAVE.getValue());
    }

    HashMap<ApiParameter, String> parameters = new HashMap<>();
    parameters.put(FlutterwaveParameter.TRANSACTION_REFERENCE, transactionReference);

    URI uri = buildUri(parameters, FlutterwaveEndpointBlock.TRANSACTIONS, FlutterwaveEndpointBlock.VERIFY_BY_REFERENCE);
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

    URI uri = buildUri(FlutterwaveEndpointBlock.TRANSACTIONS, buildPathVar(request.getTransactionId()), FlutterwaveEndpointBlock.REFUND);
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
    parameters.put(FlutterwaveParameter.AMOUNT, request.getAmount());
    parameters.put(FlutterwaveParameter.SOURCE_CURRENCY, request.getSourceCurrency());
    parameters.put(FlutterwaveParameter.DESTINATION_CURRENCY, request.getDestinationCurrency());

    URI uri = buildUri(parameters, FlutterwaveEndpointBlock.TRANSFERS, FlutterwaveEndpointBlock.RATES);
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

    URI uri = buildUri(FlutterwaveEndpointBlock.TRANSFERS);
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

    URI uri = buildUri(FlutterwaveEndpointBlock.TRANSFERS, buildPathVar(transferId), FlutterwaveEndpointBlock.RETRIES);
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

    URI uri = buildUri(FlutterwaveEndpointBlock.BANKS, buildPathVar(bankId), FlutterwaveEndpointBlock.BRANCHES);
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
  public FwGetTransferFeeResponse getTransferFee(FwGetTransferFeeRequest request) {
    if (!isMandatoryFieldAvailable(request.getAmount(), request.getCurrency())) {
      throw new ExternalSystemException(PaymentGatewayType.FLUTTERWAVE.getValue());
    }

    HashMap<ApiParameter, String> parameters = new HashMap<>();
    parameters.put(FlutterwaveParameter.AMOUNT, request.getAmount());
    parameters.put(FlutterwaveParameter.CURRENCY, request.getCurrency());

    URI uri = buildUri(parameters, FlutterwaveEndpointBlock.TRANSFERS, FlutterwaveEndpointBlock.FEE);
    ResponseEntity<FwGetTransferFeeResponse> response = doCall(uri, HttpMethod.GET,
      getAuthHeaderWithBearerToken(config.getSecretKey()), null, FwGetTransferFeeResponse.class);

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
