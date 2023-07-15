package com.umulam.fleen.health.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
public class PasswordGenerator {

  private static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
  private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final String NUMBERS = "0123456789";
  private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{};:,.<>/?";

  public String generatePassword() {
    return generatePassword(10);
  }

  public String generatePassword(int length) {
    if (length < 8) {
      length = 8;
    }
    List<Character> passwordChars = new ArrayList<>();

    Random random = new SecureRandom();
    int index;

    // Add at least one lowercase character
    index = random.nextInt(LOWER_CASE.length());
    passwordChars.add(LOWER_CASE.charAt(index));

    // Add at least one uppercase character
    index = random.nextInt(UPPER_CASE.length());
    passwordChars.add(UPPER_CASE.charAt(index));

    // Add at least one number
    index = random.nextInt(NUMBERS.length());
    passwordChars.add(NUMBERS.charAt(index));

    // Fill the remaining characters
    int remainingLength = length - 3; // Subtracting 3 for lowercase, uppercase, and number
    for (int i = 0; i < remainingLength; i++) {
      String allCharacters = LOWER_CASE + UPPER_CASE + NUMBERS + SPECIAL_CHARACTERS;
      index = random.nextInt(allCharacters.length());
      passwordChars.add(allCharacters.charAt(index));
    }

    // Shuffle the password characters
    StringBuilder password = new StringBuilder();
    while (!passwordChars.isEmpty()) {
      index = random.nextInt(passwordChars.size());
      password.append(passwordChars.remove(index));
    }

    return password.toString();
  }
}
