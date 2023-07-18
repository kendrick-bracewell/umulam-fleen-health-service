package com.umulam.fleen.health.model.dto.healthsession;

import com.umulam.fleen.health.constant.session.HealthSessionStatus;
import com.umulam.fleen.health.constant.session.SessionLocation;
import com.umulam.fleen.health.model.domain.HealthSession;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.validator.*;
import lombok.*;

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

  @Size(max = 1000)
  private String comment;

  @NotNull
  @ProfessionalValid
  private String professional;

  @NotNull
  @DateValid
  @Future
  private String date;

  @NotNull
  @TimeValid
  @WorkingHour
  private String time;

  @Size(max = 500)
  private String document;

  public HealthSession toHealthSession() {
    return HealthSession.builder()
      .professional(Member.builder()
        .id(Integer.parseInt(professional)).build())
      .location(SessionLocation.REMOTE)
      .status(HealthSessionStatus.PENDING)
      .timeZone("WAT")
      .time(toTime(time))
      .date(toDate(date))
      .comment(comment)
      .documentLink(document)
      .build();
  }
}
