package com.umulam.fleen.health.service;

public interface MfaService {

  String generateSecretKey();

  String getQrCode(final String secret);

  boolean verifyOtp(final String code, final String secret);

  String generateVerificationOtp(int length);
}
