package com.umulam.fleen.health.model.view.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.model.view.base.FleenHealthView;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionTransactionView extends FleenHealthView {

  private Integer id;
  private String reference;
  private String status;

  @JsonProperty("payment_gateway")
  private String paymentGateway;
  private String type;

  @JsonProperty("sub_type")
  private String subType;
  private Double amount;
  private String currency;

  @JsonProperty("session_reference")
  private String sessionReference;

  @JsonProperty("external_reference")
  private String externalReference;
}
