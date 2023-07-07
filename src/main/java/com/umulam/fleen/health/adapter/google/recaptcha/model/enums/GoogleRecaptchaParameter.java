package com.umulam.fleen.health.adapter.google.recaptcha.model.enums;

import com.umulam.fleen.health.adapter.ApiParameter;

public enum GoogleRecaptchaParameter implements ApiParameter {

  SECRET("secret"),
  RESPONSE("response");

  private final String value;

  GoogleRecaptchaParameter(String value) {
        this.value = value;
    }

  public String getValue() {
        return this.value;
    }
}
