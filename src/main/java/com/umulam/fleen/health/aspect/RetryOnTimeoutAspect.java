package com.umulam.fleen.health.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
public class RetryOnTimeoutAspect {

  private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

  @Around("@annotation(retryOnTimeout)")
  public Object retryOnTimeout(ProceedingJoinPoint joinPoint, RetryOnTimeout retryOnTimeout) throws Throwable {
    int maxAttempts = retryOnTimeout.maxAttempts();
    long timeoutMillis = retryOnTimeout.timeoutMillis();
    int attempts = 0;

    do {
      try {
        return joinPoint.proceed();
      } catch (Throwable throwable) {
        attempts++;
        if (attempts >= maxAttempts) {
          throw throwable;
        }

        scheduleRetry(joinPoint, retryOnTimeout, attempts, timeoutMillis);
      }
    } while (attempts < maxAttempts);

    return null;
  }

  private void scheduleRetry(ProceedingJoinPoint joinPoint, RetryOnTimeout retryOnTimeout, int attempt, long timeoutMillis) {
    executorService.schedule(() -> {
      try {
        joinPoint.proceed();
      } catch (Throwable throwable) {
        log.error(throwable.getMessage());
      }
    }, timeoutMillis, TimeUnit.MILLISECONDS);
  }
}