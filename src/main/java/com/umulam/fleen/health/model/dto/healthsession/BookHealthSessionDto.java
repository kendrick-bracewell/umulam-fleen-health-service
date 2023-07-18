package com.umulam.fleen.health.model.dto.healthsession;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookHealthSessionDto {

  @Size(min = 0, max = 1000)
  private String comment;

  @NotNull
  @
  private String professional;
  private String date;
  private String time;
  private String document;
}
