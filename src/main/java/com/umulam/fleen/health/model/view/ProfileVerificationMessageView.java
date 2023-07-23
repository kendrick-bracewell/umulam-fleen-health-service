package com.umulam.fleen.health.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.model.view.base.FleenHealthView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileVerificationMessageView extends FleenHealthView {

  private String title;
  private String message;

  @JsonProperty("verification_message_type")
  private String verificationMessageType;

  @JsonProperty("html_message")
  private String htmlMessage;

  @JsonProperty("plain_text")
  private String plainText;
}
