package com.umulam.fleen.health.controller;

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

  private final PaystackConfig config;
  private final TransactionValidationService transactionValidationService;

  public TransactionController(PaystackConfig config,
                               TransactionValidationService transactionValidationService) {
    this.config = config;
    this.transactionValidationService = transactionValidationService;
  }

  @Async
  @PostMapping(value = "/payment/verification")
  public CompletableFuture<FleenHealthResponse> validateAndCompletePaymentTransaction(@RequestBody String body, HttpServletRequest request) {
    if (config.getIpWhitelist().contains(request.getHeader(X_FORWARDED_HEADER))) {
      transactionValidationService.validateAndCompleteTransaction(body);
    }
    return CompletableFuture.completedFuture(new FleenHealthResponse(SUCCESS_MESSAGE));
  }

}
