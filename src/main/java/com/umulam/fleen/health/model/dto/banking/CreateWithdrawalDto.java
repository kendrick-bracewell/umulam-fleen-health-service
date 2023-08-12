package com.umulam.fleen.health.model.dto.banking;

import lombok.*;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateWithdrawalDto {

  @NotNull(message = "{banking.withdrawal.amount.notNull}")
  @DecimalMin(value = "1000.00", message = "{banking.withdrawal.amount.min}")
  private BigDecimal amount;

  @NotBlank(message = "{banking.accountNumber.notNull}")
  @Size(min = 10, max = 10, message = "{banking.accountNumber.size}")
  private String accountNumber;


}
