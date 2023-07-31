package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.adapter.flutterwave.model.response.FwGetExchangeRateResponse;
import com.umulam.fleen.health.service.ExchangeRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

  private final ConfigService configService;
  private final FlutterwaveService flutterwaveService;

  public ExchangeRateServiceImpl(ConfigService configService,
                                 FlutterwaveService flutterwaveService) {
    this.configService = configService;
    this.flutterwaveService = flutterwaveService;
  }

  @Override
  public Double getConvertedHealthSessionPrice(Double amount) {
    FwGetExchangeRateResponse exchangeRate = flutterwaveService.getExchangeRate(amount, configService.getPaymentCurrency(), configService.getPricingCurrency());
    return exchangeRate.getData().getSource().getAmount();
  }
}
