package com.umulam.fleen.health.aspect;

import com.umulam.fleen.health.adapter.google.recaptcha.model.response.ReCaptchaResponse;
import com.umulam.fleen.health.exception.authentication.InvalidReCaptchaException;
import com.umulam.fleen.health.service.AuthenticationService;
import com.umulam.fleen.health.service.external.google.ReCaptchaAttemptService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * The ReCaptchaAspect is used on the application APIs to verify that requests coming to the application are
 * made by a real user or human and not a machine. It also protects the application APIs from unnecessary calls in case
 * of invalid security tokens attached to the requests sent to application, it sets a limit on the number of requests to be
 * made at an instance to the application APIs until after a defined period of time.
 *
 * @author Yusuf Alamu Musa
 */
@Aspect
@Component
public class ReCaptchaAspect {

  private final AuthenticationService authenticationService;
  private final ReCaptchaAttemptService reCaptchaAttemptService;
  private final String scoreThreshold;

  public ReCaptchaAspect(AuthenticationService authenticationService,
                         ReCaptchaAttemptService reCaptchaAttemptService,
                         @Value("${google.recaptcha.score-threshold}") String scoreThreshold) {
    this.authenticationService = authenticationService;
    this.reCaptchaAttemptService = reCaptchaAttemptService;
    this.scoreThreshold = scoreThreshold;
  }

  private static final String RECAPTCHA_HEADER_KEY = "recaptcha-response";

  @Around("@annotation(ReCaptcha)")
  public Object verifyReCaptcha(ProceedingJoinPoint joinPoint) throws Throwable {

    String ipAddress = getRequest().getRemoteAddr();
    if (reCaptchaAttemptService.isBlocked(ipAddress)) {
      throw new InvalidReCaptchaException("ReCaptcha attempt failed");
    }

    String reCaptchaToken = getRequest().getHeader(RECAPTCHA_HEADER_KEY);
    ReCaptchaResponse response = authenticationService.verifyReCaptcha(reCaptchaToken);
    if (Objects.nonNull(response)
        && response.isSuccess()
        && response.getScore() >= getReCaptchaScoreThreshold()) {
        reCaptchaAttemptService.reCaptchaSucceeded(ipAddress);
        return joinPoint.proceed();
    }

    reCaptchaAttemptService.reCaptchaFailed(ipAddress);
    throw new InvalidReCaptchaException(null);
  }

  private float getReCaptchaScoreThreshold() {
    return Float.parseFloat(this.scoreThreshold);
  }

  private HttpServletRequest getRequest() {
    return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
  }
}
