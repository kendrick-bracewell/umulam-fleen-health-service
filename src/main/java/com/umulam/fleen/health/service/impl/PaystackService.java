package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.adapter.paystack.PaystackAdapter;
import com.umulam.fleen.health.adapter.paystack.response.GetBanksResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.umulam.fleen.health.constant.base.GeneralConstant.PAYSTACK_GET_BANKS_CACHE_PREFIX;

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
    String cacheKey = PAYSTACK_GET_BANKS_CACHE_PREFIX;
    if (cacheService.exists(cacheKey)) {
      return cacheService.get(cacheKey, GetBanksResponse.class).getData();
    }

    GetBanksResponse banksResponse = paystackAdapter.getBanks(currency);
    cacheService.set(cacheKey, banksResponse);
    return banksResponse.getData();
  }

  public
}
