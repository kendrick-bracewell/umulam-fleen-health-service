package com.umulam.fleen.health.adapter.google.recaptcha.model.enums;

import com.umulam.fleen.health.adapter.EndpointBlock;

public enum GoogleRecaptchaEndpointBlock implements EndpointBlock {

  SITE_VERIFY("/siteverify");

  private final String value;

  GoogleRecaptchaEndpointBlock(String value) {
        this.value = value;
    }

  public String getValue() {
        return this.value;
    }
}
