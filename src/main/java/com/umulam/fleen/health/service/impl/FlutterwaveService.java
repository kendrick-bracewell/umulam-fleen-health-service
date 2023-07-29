package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.adapter.flutterwave.FlutterwaveAdapter;
import com.umulam.fleen.health.adapter.flutterwave.model.enums.FwBankCountryType;
import com.umulam.fleen.health.adapter.flutterwave.response.FwGetBanksResponse;
import com.umulam.fleen.health.repository.jpa.BankAccountJpaRepository;
import com.umulam.fleen.health.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.umulam.fleen.health.constant.base.GeneralConstant.FLUTTERWAVE_GET_BANKS_CACHE_PREFIX;
import static java.util.Objects.isNull;

@Slf4j
@Service
public class FlutterwaveService {

  private final FlutterwaveAdapter flutterwaveAdapter;
  private final CacheService cacheService;
  private final BankAccountJpaRepository bankAccountJpaRepository;
  private final MemberService memberService;

  public FlutterwaveService(FlutterwaveAdapter flutterwaveAdapter,
                         CacheService cacheService,
                         BankAccountJpaRepository bankAccountJpaRepository,
                         MemberService memberService) {
    this.flutterwaveAdapter = flutterwaveAdapter;
    this.cacheService = cacheService;
    this.bankAccountJpaRepository = bankAccountJpaRepository;
    this.memberService = memberService;
  }

  public List<FwGetBanksResponse.FwBankData> getBanks(String country) {
    String cacheKey = getBanksCacheKey().concat(country.toUpperCase());
    if (cacheService.exists(cacheKey)) {
      return cacheService.get(cacheKey, FwGetBanksResponse.class).getData();
    }

    FwGetBanksResponse banksResponse = flutterwaveAdapter.getBanks(country);
    cacheService.set(cacheKey, banksResponse);
    return banksResponse.getData();
  }

  @EventListener(ApplicationReadyEvent.class)
  public void saveBanksToCacheOnStartup() {
    FwGetBanksResponse banksResponse = flutterwaveAdapter.getBanks(FwBankCountryType.NG.getValue());
    saveBanksFwToCache(banksResponse, null);
  }

  @Scheduled(cron = "0 0 */12 * * *")
  private void saveBanksToCache() {
    saveBanksFwToCache(null, FwBankCountryType.NG.getValue());
  }

  private void saveBanksFwToCache(FwGetBanksResponse response, String country) {
    if (isNull(response) || isNull(response.getData()) || response.getData().isEmpty()) {
      saveBanksToCacheOnStartup();
    }
    String key = getBanksCacheKey().concat(Objects.toString(country, FwBankCountryType.NG.getValue()));
    cacheService.set(key, response);
  }

  private String getBanksCacheKey() {
    return FLUTTERWAVE_GET_BANKS_CACHE_PREFIX;
  }

}
