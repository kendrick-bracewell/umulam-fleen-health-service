package com.umulam.fleen.health.adapter.flutterwave.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FwGetBanksResponse extends FlutterwaveResponse {

  private List<FwBankData> data;

  @Getter
  @Setter
  @NoArgsConstructor
  public static class FwBankData {
    private String id;
    private String name;
    private String code;
  }
}
