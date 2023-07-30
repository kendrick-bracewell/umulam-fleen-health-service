package com.umulam.fleen.health.adapter.flutterwave.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FwGetBankBranchesResponse extends FlutterwaveResponse {

  private List<GetBankBranchesData> data;

  @Getter
  @Setter
  @NoArgsConstructor
  public static class GetBankBranchesData {

    private String id;

    @JsonProperty("branch_code")
    private String branchCode;

    @JsonProperty("branch_name")
    private String branchName;

    @JsonProperty("swift_code")
    private String swiftCode;

    @JsonProperty("bic")
    private String bic;

    @JsonProperty("bank_id")
    private String bankId;
  }
}
