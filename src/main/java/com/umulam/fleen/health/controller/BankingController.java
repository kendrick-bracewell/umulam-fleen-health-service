package com.umulam.fleen.health.controller;

import com.umulam.fleen.health.service.impl.PaystackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "banking")
public class BankingController {

  private final PaystackService paystackService;

  public BankingController(PaystackService paystackService) {
    this.paystackService = paystackService;
  }

  @GetMapping(value = "/get-banks")
  public Object getBanks(@RequestParam(name = "currency", defaultValue = "NGN") String currency) {
    return paystackService.getBanks(currency);
  }
}
