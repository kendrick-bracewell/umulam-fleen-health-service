package com.umulam.fleen.health.service;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@AllArgsConstructor
public class CacheService {

  private final RedisTemplate<String, Object> redisTemplate;

  public boolean exists(String key) {
    return Boolean.TRUE.equals(redisTemplate.hasKey(key));
  }

  public Object get(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  public Object getByHash(String hash, String key) {
    return redisTemplate.opsForHash().get(key, hash);
  }

  public void set(String key, Object value) {
    redisTemplate.opsForValue().set(key, value);
  }

  public void set(String key, Object value, Duration duration) {
    set(key, value);
    expire(key, duration);
  }

  public void setByHash(String hash, String key, Object value) {
    redisTemplate.opsForHash().put(hash, key, value);
  }

  public void expire(String key, Duration duration) {
    redisTemplate.expire(key, duration);
  }

  public void delete(String key) {
    redisTemplate.delete(key);
  }

  public void deleteByHash(String hash, String key) {
    redisTemplate.opsForHash().delete(key, hash);
  }

}
