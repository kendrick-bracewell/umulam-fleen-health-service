package com.umulam.fleen.health.controller;

import com.umulam.fleen.health.adapter.flutterwave.config.FlutterwaveConfig;
import com.umulam.fleen.health.adapter.paystack.config.PaystackConfig;
import com.umulam.fleen.health.model.response.FleenHealthResponse;
import com.umulam.fleen.health.service.transaction.TransactionValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.SUCCESS_MESSAGE;
import static com.umulam.fleen.health.constant.base.GeneralConstant.X_FORWARDED_HEADER;

@Slf4j
@RestController
@RequestMapping(value = "transaction")
public class TransactionController {

  private final PaystackConfig psConfig;
  private final FlutterwaveConfig fwConfig;
  private final TransactionValidationService transactionValidationService;

  public TransactionController(PaystackConfig psConfig,
                               FlutterwaveConfig fwConfig,
                               TransactionValidationService transactionValidationService) {
    this.psConfig = psConfig;
    this.fwConfig = fwConfig;
    this.transactionValidationService = transactionValidationService;
  }

  @Async
  @PostMapping(value = "/payment/verification/ps")
  public CompletableFuture<FleenHealthResponse> validateAndCompletePaymentTransactionPs(@RequestBody String body, HttpServletRequest request) {
    if (psConfig.getIpWhitelist().contains(request.getHeader(X_FORWARDED_HEADER))) {
      transactionValidationService.validateAndCompleteTransaction(body);
    }
    return CompletableFuture.completedFuture(new FleenHealthResponse(SUCCESS_MESSAGE));
  }

  @Async
  @PostMapping(value = "/payment/verification/fw")
  public CompletableFuture<FleenHealthResponse> validateAndCompletePaymentTransactionFw(@RequestBody String body, HttpServletRequest request) {
    if (fwConfig.getSecretHash().equalsIgnoreCase(request.getHeader(fwConfig.getVerificationHeader()))) {
      transactionValidationService.validateAndCompleteTransaction(body);
    }
    return CompletableFuture.completedFuture(new FleenHealthResponse(SUCCESS_MESSAGE));
  }

}
