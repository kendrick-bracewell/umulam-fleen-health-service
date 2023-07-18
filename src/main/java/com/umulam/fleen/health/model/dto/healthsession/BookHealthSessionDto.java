package com.umulam.fleen.health.model.dto.healthsession;

import com.umulam.fleen.health.validator.DateValid;
import com.umulam.fleen.health.validator.MemberExists;
import com.umulam.fleen.health.validator.TimeValid;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookHealthSessionDto {

  @Size(max = 1000)
  private String comment;

  @NotNull
  @MemberExists
  private String professional;


  @NotNull
  @DateValid
  private String date;

  @NotNull
  @TimeValid
  private String time;

  @Size(max = 500)
  private String document;
}
