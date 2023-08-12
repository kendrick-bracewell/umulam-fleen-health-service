package com.umulam.fleen.health.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.REFERENCE_PREFIX;
import static com.umulam.fleen.health.constant.base.FleenHealthConstant.TRANSACTION_REFERENCE_PREFIX;

@Slf4j
@Component
public class FleenHealthReferenceGenerator {

  private final UniqueReferenceGenerator referenceGenerator;

  public FleenHealthReferenceGenerator(UniqueReferenceGenerator referenceGenerator) {
    this.referenceGenerator = referenceGenerator;
  }

  public String generateSessionReference() {
    return REFERENCE_PREFIX.concat(referenceGenerator.generateUniqueReference());
  }

  public String generateTransactionReference() {
    return TRANSACTION_REFERENCE_PREFIX.concat(referenceGenerator.generateUniqueReference());
  }

  public String generateGroupTransactionReference() {
    return TRANSACTION_REFERENCE_PREFIX.concat(referenceGenerator.generateUniqueReferenceLong());
  }
}
