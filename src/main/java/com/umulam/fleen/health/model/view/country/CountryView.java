package com.umulam.fleen.health.model.view.country;

import com.umulam.fleen.health.model.view.base.FleenHealthView;
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
