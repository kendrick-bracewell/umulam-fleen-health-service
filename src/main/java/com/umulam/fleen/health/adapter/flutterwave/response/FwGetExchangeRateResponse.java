package com.umulam.fleen.health.adapter.flutterwave.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FwGetExchangeRateResponse extends FlutterwaveResponse {

  private ExchangeRateData data;

  @Getter
  @Setter
  @NoArgsConstructor
  public static class ExchangeRateData {

    private Double rate;
    private SourceCurrency source;
    private DestinationCurrency destination;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SourceCurrency {

      private String currency;
      private Double amount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DestinationCurrency {

      private String currency;
      private Double amount;
    }
  }
}
