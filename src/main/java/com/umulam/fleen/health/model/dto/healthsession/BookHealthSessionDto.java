package com.umulam.fleen.health.model.dto.healthsession;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.session.HealthSessionStatus;
import com.umulam.fleen.health.constant.session.SessionLocation;
import com.umulam.fleen.health.model.domain.HealthSession;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.validator.*;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.umulam.fleen.health.util.DateTimeUtil.toDate;
import static com.umulam.fleen.health.util.DateTimeUtil.toTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookHealthSessionDto {

  @Size(max = 1000, message = "{session.comment.size}")
  private String comment;

  @NotNull(message = "{session.professional.notNull}")
  @ProfessionalValid(message = "{session.professional.valid}")
  private String professional;

  @NotNull(message = "{session.date.notNull}")
  @DateValid(message = "{session.date.valid}")
  @Future
  private String date;

  @NotNull(message = "{session.time.notNull}")
  @TimeValid(message = "{session.time.valid}")
  @WorkingHour(message = "{session.time.workingHour}")
  private String time;

  @URL(message = "{session.document.isUrl}")
  @Size(max = 500, message = "{session.document.size}")
  private String document;

  @Valid
  @NotNull(message = "{session.transaction.notNull}")
  @JsonProperty("transaction_data")
  private TransactionData transactionData;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class TransactionData {

    @NotNull(message = "{session.transaction.amount.notNull}")
    private Double amount;
  }

  public HealthSession toHealthSession() {
    return HealthSession.builder()
      .professional(Member.builder()
        .id(Integer.parseInt(professional)).build())
      .location(SessionLocation.REMOTE)
      .status(HealthSessionStatus.PENDING)
      .timezone("WAT")
      .time(toTime(time))
      .date(toDate(date))
      .comment(comment)
      .documentLink(document)
      .build();
  }
}
