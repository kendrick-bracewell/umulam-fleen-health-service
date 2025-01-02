package com.umulam.fleen.health.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.model.view.base.FleenHealthView;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerificationDocumentView extends FleenHealthView {

  private String filename;
  private String link;

  @JsonProperty("download_link")
  private String downloadLink;

  @JsonProperty("document_type")
  private String documentType;

}
 
 
 
