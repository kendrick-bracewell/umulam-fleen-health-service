package com.umulam.fleen.health.adapter.base;

import com.umulam.fleen.health.adapter.EndpointBlock;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseEndpointBlock implements EndpointBlock {

  private final String value;
}
