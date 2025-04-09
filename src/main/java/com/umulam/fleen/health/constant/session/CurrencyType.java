package com.umulam.fleen.health.constant.session;

import com.umulam.fleen.health.adapter.ApiParameter;
import lombok.Getter;

@Getter
public enum CurrencyType implements ApiParameter {

  NGN("NGN", "NG", "Nigeria"),
  GHS("GHS", "GH", "Ghana"),
  ZAR("ZAR", "ZA", "South Africa"),
  KES("KES", "KE", "Kenya"),
  UGX("UGX", "UG", "Uganda");

  private final String value;
  private final String country;
  private final String countryName;

  CurrencyType(String value, String country, String countryName) {
    this.value = value;
    this.country = country;
    this.countryName = countryName;
  }

  @Override
  public String getValue() {
    return value;
  }
}
 
