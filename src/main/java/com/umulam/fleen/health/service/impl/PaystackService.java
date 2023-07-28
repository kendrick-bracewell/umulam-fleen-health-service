package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.adapter.paystack.PaystackAdapter;
import com.umulam.fleen.health.adapter.paystack.response.GetBanksResponse;
import com.umulam.fleen.health.constant.session.CurrencyType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.umulam.fleen.health.constant.base.GeneralConstant.PAYSTACK_GET_BANKS_CACHE_PREFIX;
import static java.util.Objects.isNull;

@Slf4j
@Service
public class PaystackService {

  private final PaystackAdapter paystackAdapter;
  private final CacheService cacheService;

  public PaystackService(PaystackAdapter paystackAdapter,
                         CacheService cacheService) {
    this.paystackAdapter = paystackAdapter;
    this.cacheService = cacheService;
  }

  public Object getBanks(String currency) {
    String cacheKey = getBanksCacheKey().concat(currency.toUpperCase());
    if (cacheService.exists(cacheKey)) {
      return cacheService.get(cacheKey, GetBanksResponse.class).getData();
    }

    GetBanksResponse banksResponse = paystackAdapter.getBanks(currency);
    cacheService.set(cacheKey, banksResponse);
    return banksResponse.getData();
  }

  @EventListener(ApplicationReadyEvent.class)
  public void saveBanksToCacheOnStartup() {
    GetBanksResponse banksResponse = paystackAdapter.getBanks(CurrencyType.NGN.getValue());
    saveBanksToCache(banksResponse, null);
  }

  @Scheduled(cron = "0 0 */12 * * *")
  private void saveBanksToCache() {
    saveBanksToCache(null, CurrencyType.NGN.getValue());
  }

  private void saveBanksToCache(GetBanksResponse response, String currency) {
    if (isNull(response) || isNull(response.getData()) || response.getData().isEmpty()) {
      saveBanksToCacheOnStartup();
    }
    String key = getBanksCacheKey().concat(Objects.toString(currency, CurrencyType.NGN.getValue()));
    cacheService.set(key, response);
  }

  private String getBanksCacheKey() {
    return PAYSTACK_GET_BANKS_CACHE_PREFIX;
  }

}
