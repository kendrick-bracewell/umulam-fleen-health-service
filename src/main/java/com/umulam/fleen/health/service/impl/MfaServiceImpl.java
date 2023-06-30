package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.service.MfaService;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

import static com.umulam.fleen.health.constant.authentication.AuthenticationConstant.MFA_SECRET_ISSUER;
import static com.umulam.fleen.health.constant.authentication.AuthenticationConstant.MFA_SECRET_LABEL;

@Slf4j
@Component
public class MfaServiceImpl implements MfaService {

  public final SecretGenerator secretGenerator;
  private final QrGenerator qrGenerator;
  private final CodeVerifier codeVerifier;

  public MfaServiceImpl(SecretGenerator secretGenerator,
                        QrGenerator qrGenerator,
                        CodeVerifier codeVerifier) {
    this.secretGenerator = secretGenerator;
    this.qrGenerator = qrGenerator;
    this.codeVerifier = codeVerifier;
  }

  @Override
  public String generateSecretKey() {
    return secretGenerator.generate();
  }

  @Override
  public String getQrCode(String secret) {
    QrData data = new QrData
            .Builder()
            .secret(secret)
            .label(MFA_SECRET_LABEL)
            .issuer(MFA_SECRET_ISSUER)
            .algorithm(HashingAlgorithm.SHA256)
            .digits(6)
            .period(30)
            .build();
    try {
      return Utils
              .getDataUriForImage(
                      qrGenerator.generate(data),
                      qrGenerator.getImageMimeType());
    } catch (QrGenerationException ex) {
      log.error(ex.getMessage(), ex);
    }
    return null;
  }

  @Override
  public boolean verifyOtp(String code, String secret) {
    return codeVerifier.isValidCode(secret, code);
  }

  @Override
  public String generateVerificationOtp(int length) {
    Random obj = new Random();
    char[] otp = new char[6];

    for (int i = 0; i < 6; i++)  {
      otp[i]= (char) (obj.nextInt(10) + 48);
    }
    return String.valueOf(otp);
  }
}
