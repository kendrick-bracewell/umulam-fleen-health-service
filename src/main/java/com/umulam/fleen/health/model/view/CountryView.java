package com.umulam.fleen.health.model.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

import static com.umulam.fleen.health.util.DateFormatUtil.DATE_TIME_FORMAT;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CountryView {

  private Integer id;
  private String title;
  private String code;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
  private LocalDateTime createdOn;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
  private LocalDateTime updatedOn;
}
