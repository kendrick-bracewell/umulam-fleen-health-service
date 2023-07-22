package com.umulam.fleen.health.model.dto.healthsession;

import com.umulam.fleen.health.constant.session.HealthSessionStatus;
import com.umulam.fleen.health.constant.session.SessionLocation;
import com.umulam.fleen.health.model.domain.HealthSession;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.validator.*;
import lombok.*;

import javax.validation.constraints.NotNull;

import static com.umulam.fleen.health.util.DateTimeUtil.toDate;
import static com.umulam.fleen.health.util.DateTimeUtil.toTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReScheduleHealthSessionDto {

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

  public HealthSession toHealthSession() {
    return HealthSession.builder()
      .professional(Member.builder()
        .id(Integer.parseInt(professional)).build())
      .timeZone("WAT")
      .time(toTime(time))
      .date(toDate(date))
      .build();
  }
}
