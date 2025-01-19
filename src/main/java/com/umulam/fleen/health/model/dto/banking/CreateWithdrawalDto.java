package com.umulam.fleen.health.model.dto.banking;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.validator.BankAccountExist;
import com.umulam.fleen.health.validator.IsNumber;
import lombok.*;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateWithdrawalDto {

  @NotNull(message = "{banking.withdrawal.amount.notNull}")
  @DecimalMin(value = "20.00", message = "{banking.withdrawal.amount.min}")
  private BigDecimal amount;

  @NotBlank(message = "{banking.bankAccount.notNull}")
  @IsNumber(message = "{banking.bankAccount.isNumber}")
  @BankAccountExist(message = "{banking.bankAccount.exists}")
  @JsonProperty("bank_account")
  private String bankAccount;
}
 
 
