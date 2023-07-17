package com.umulam.fleen.health.model.view;

import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CountryView extends FleenHealthView {

  private String title;
  private String code;

}
