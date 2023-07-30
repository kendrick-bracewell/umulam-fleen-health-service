package com.umulam.fleen.health.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.UUID;

@Component
public class UniqueReferenceGenerator {

  private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

  public String generateUniqueReference(int length) {
    if (length <= 0 || length > 25) {
      throw new IllegalArgumentException("Length should be between 1 and 25");
    }

    SecureRandom random = new SecureRandom();
    StringBuilder stringBuilder = new StringBuilder();

    for (int i = 0; i < length; i++) {
      int randomIndex = random.nextInt(CHARACTERS.length());
      stringBuilder.append(CHARACTERS.charAt(randomIndex));
    }

    return stringBuilder.toString();
  }

  public String generateUniqueReference() {
    String uuidPart = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    String randomPart = generateUniqueReference(10);

    return uuidPart + randomPart;
  }

  public String generateUniqueReferenceLong() {
    String uuidPart = UUID.randomUUID().toString().replace("-", "").substring(0, 15);
    String randomPart = generateUniqueReference(20);

    return uuidPart + randomPart;
  }
}
