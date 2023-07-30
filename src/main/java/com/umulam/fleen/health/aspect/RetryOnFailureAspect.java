package com.umulam.fleen.health.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RetryOnFailureAspect {

  @Around("@annotation(retryOnFailure)")
  public Object retryOnTimeout(ProceedingJoinPoint joinPoint, RetryOnFailure retryOnFailure) throws Throwable {
    int maxAttempts = retryOnFailure.maxAttempts();
    long timeoutMillis = retryOnFailure.timeoutMillis();
    int attempts = 0;

    do {
      try {
        return joinPoint.proceed();
      } catch (Throwable throwable) {
        if (attempts >= maxAttempts) {
          throw throwable;
        }

        attempts++;
        Thread.sleep(timeoutMillis);
      }
    } while (attempts < maxAttempts);

    return null;
  }
}