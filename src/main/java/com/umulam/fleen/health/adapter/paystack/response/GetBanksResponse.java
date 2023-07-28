package com.umulam.fleen.health.adapter.paystack.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetBanksResponse {

  private boolean status;
  private String message;
  private List<BankData> data;

  @Getter
  @Setter
  @NoArgsConstructor
  public static class BankData {
    private String name;
    private String code;
    private String currency;
  }
}
