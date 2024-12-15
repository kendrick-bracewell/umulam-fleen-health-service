package com.umulam.fleen.health.model.view.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionTransactionViewBasic extends SessionTransactionView {
}
 
