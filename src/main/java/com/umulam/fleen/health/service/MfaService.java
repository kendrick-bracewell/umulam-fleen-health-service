package com.umulam.fleen.health.service;

public interface MfaService {

  String generateSecretKey();

  String getQrCode(final String secret);

  boolean verifyAuthenticatorOtp(final String code, final String secret);

  String generateVerificationOtp(int length);
}
