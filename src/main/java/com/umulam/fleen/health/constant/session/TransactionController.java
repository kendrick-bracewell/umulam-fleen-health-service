package com.umulam.fleen.health.constant.session;

import com.umulam.fleen.health.model.response.FleenHealthResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.SUCCESS_MESSAGE;

@Slf4j
@RestController
@RequestMapping(value = "transaction")
public class TransactionController {

  @

  @Async
  @PostMapping(value = "/session-payment/verification")
  public CompletableFuture<FleenHealthResponse> validateAndCompletePaymentTransaction(HttpServletRequest request) {
    if ()
    return CompletableFuture.completedFuture(new FleenHealthResponse(SUCCESS_MESSAGE));
  }
}
