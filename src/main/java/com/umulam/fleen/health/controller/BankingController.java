package com.umulam.fleen.health.controller;

import com.umulam.fleen.health.model.dto.banking.AddBankAccountDto;
import com.umulam.fleen.health.model.response.FleenHealthResponse;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.service.impl.PaystackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.BANK_ACCOUNT_DETAILS_SAVED;

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

  @PostMapping(value = "/add-account")
  public Object addBankAccount(@Valid @RequestBody AddBankAccountDto dto, FleenUser user) {
    paystackService.addBankAccount(dto, user);
    return new FleenHealthResponse(BANK_ACCOUNT_DETAILS_SAVED);
  }
}
