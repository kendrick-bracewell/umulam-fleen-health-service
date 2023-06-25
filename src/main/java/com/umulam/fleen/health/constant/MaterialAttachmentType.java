package com.umulam.fleen.health.constant;

import lombok.Getter;

@Getter
public enum MaterialAttachmentType {

  ABSTRACT("Abstract"),
  DOCUMENT("Document");

  private final String value;

  MaterialAttachmentType(String value) {
    this.value = value;
  }
}
