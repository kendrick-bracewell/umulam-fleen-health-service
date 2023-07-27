package com.umulam.fleen.health.model.request.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.member.ProfessionalQualificationType;
import com.umulam.fleen.health.constant.session.TransactionStatus;
import com.umulam.fleen.health.constant.session.TransactionSubType;
import com.umulam.fleen.health.model.request.search.base.SearchRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionSearchRequest extends SearchRequest {

  private String reference;

  @JsonProperty("transaction_status")
  private TransactionStatus transactionStatus;

  @JsonProperty("transaction_sub_type")
  private TransactionSubType transactionSubType;

  @JsonProperty("qualification")
  private ProfessionalQualificationType qualificationType;
}
