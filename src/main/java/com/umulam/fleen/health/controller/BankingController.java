package com.umulam.fleen.health.controller;

import com.umulam.fleen.health.model.dto.banking.AddBankAccountDto;
import com.umulam.fleen.health.model.dto.banking.CreateWithdrawalDto;
import com.umulam.fleen.health.model.response.FleenHealthResponse;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.BankAccountView;
import com.umulam.fleen.health.service.BankingService;
import com.umulam.fleen.health.service.external.banking.FlutterwaveService;
import com.umulam.fleen.health.service.external.banking.PaystackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.BANK_ACCOUNT_DETAILS_DELETED;
import static com.umulam.fleen.health.constant.base.FleenHealthConstant.BANK_ACCOUNT_DETAILS_SAVED;

@Slf4j
@RestController
@RequestMapping(value = "banking")
public class BankingController {

  private final PaystackService paystackService;
  private final FlutterwaveService flutterwaveService;
  private final BankingService bankingService;

  public BankingController(@Qualifier("paystackService") PaystackService paystackService,
                           @Qualifier("flutterwaveService") FlutterwaveService flutterwaveService,
                           BankingService bankingService) {
    this.paystackService = paystackService;
    this.flutterwaveService = flutterwaveService;
    this.bankingService = bankingService;
  }

  @GetMapping(value = "/get-banks-ps")
  public Object getBanksPs(@RequestParam(name = "currency", defaultValue = "NGN") String currency) {
    return paystackService.getBanks(currency);
  }

  @GetMapping(value = "/get-supported-countries")
  public Object getSupportedCountries() {
    return null;
  }

  @GetMapping(value = "/get-banks-fw")
  public Object getBanksFw(@RequestParam(name = "country", defaultValue = "NG") String country) {
    return flutterwaveService.getBanks(country);
  }

  @GetMapping(value = "/bank-account/entries")
  public List<BankAccountView> findBankAccounts(@AuthenticationPrincipal FleenUser user) {
    return bankingService.getBankAccounts(user);
  }

  @GetMapping(value = "/bank-account/detail/{id}")
  public BankAccountView findBankAccount(@PathVariable(name = "id") Integer bankAccountId, @AuthenticationPrincipal FleenUser user) {
    return bankingService.getBankAccount(user, bankAccountId);
  }

  @PostMapping(value = "/add-account-ps")
  public Object addBankAccountPs(@Valid @RequestBody AddBankAccountDto dto, @AuthenticationPrincipal FleenUser user) {
    paystackService.addBankAccount(dto, user);
    return new FleenHealthResponse(BANK_ACCOUNT_DETAILS_SAVED);
  }

  @PostMapping(value = "/add-account-fw")
  public Object addBankAccountFw(@Valid @RequestBody AddBankAccountDto dto, @AuthenticationPrincipal FleenUser user) {
    flutterwaveService.addBankAccount(dto, user);
    return new FleenHealthResponse(BANK_ACCOUNT_DETAILS_SAVED);
  }

  @DeleteMapping(value = "/delete-account/{accountNumber}/ps")
  public Object deleteBankAccountPs(@PathVariable(name = "accountNumber") String accountNumber, @AuthenticationPrincipal FleenUser user) {
    paystackService.deleteBankAccount(accountNumber, user);
    return new FleenHealthResponse(BANK_ACCOUNT_DETAILS_DELETED);
  }

  @DeleteMapping(value = "/delete-account/{accountNumber}/fw")
  public Object deleteBankAccountFw(@PathVariable(name = "accountNumber") String accountNumber, @AuthenticationPrincipal FleenUser user) {
    flutterwaveService.deleteBankAccount(accountNumber, user);
    return new FleenHealthResponse(BANK_ACCOUNT_DETAILS_DELETED);
  }

  @PutMapping(value = "/withdraw")
  public void withdraw(@Valid @RequestBody CreateWithdrawalDto dto, @AuthenticationPrincipal FleenUser user) {
    bankingService.createWithdrawal(dto, user);
  }

  @GetMapping(value = "/get-bank-branches-fw/{id}")
  public Object getBanksBranchesFw(@PathVariable(name = "id") Integer bankId) {
    return flutterwaveService.getBankBranches(bankId);
  }
}
