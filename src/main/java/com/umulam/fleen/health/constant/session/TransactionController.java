package com.umulam.fleen.health.constant.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umulam.fleen.health.adapter.paystack.PaystackConfig;
import com.umulam.fleen.health.model.event.paystack.ChargeEvent;
import com.umulam.fleen.health.model.response.FleenHealthResponse;
import com.umulam.fleen.health.service.HealthSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

import static com.umulam.fleen.health.constant.authentication.AuthenticationConstant.X_FORWARDED_HEADER;
import static com.umulam.fleen.health.constant.base.FleenHealthConstant.SUCCESS_MESSAGE;

@Slf4j
@RestController
@RequestMapping(value = "transaction")
public class TransactionController {

  private final PaystackConfig config;
  private final HealthSessionService healthSessionService;
  private final ObjectMapper mapper;

  public TransactionController(PaystackConfig config,
                               HealthSessionService healthSessionService,
                               ObjectMapper mapper) {
    this.config = config;
    this.healthSessionService = healthSessionService;
    this.mapper = mapper;
  }

  @Async
  @PostMapping(value = "/session-payment/verification")
  public CompletableFuture<FleenHealthResponse> validateAndCompletePaymentTransaction(@RequestBody String body, HttpServletRequest request) {
    if (config.getIpWhitelist().contains(request.getHeader(X_FORWARDED_HEADER))) {
      healthSessionService.validateAndCompletePaymentTransaction(null);
    }
    return CompletableFuture.completedFuture(new FleenHealthResponse(SUCCESS_MESSAGE));
  }
}
