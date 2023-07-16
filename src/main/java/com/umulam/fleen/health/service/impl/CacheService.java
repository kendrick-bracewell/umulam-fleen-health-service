package com.umulam.fleen.health.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umulam.fleen.health.model.domain.ProfileVerificationMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
@AllArgsConstructor
public class CacheService {

  private final RedisTemplate<String, Object> redisTemplate;
  private final ObjectMapper mapper;

  public boolean exists(String key) {
    return Boolean.TRUE.equals(redisTemplate.hasKey(key));
  }

  public Object get(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  public Object getByHash(String hash, String key) {
    return redisTemplate.opsForHash().get(key, hash);
  }

  public void set(String key, String value) {
    redisTemplate.opsForValue().set(key, value);
  }

  public void set(String key, String value, Duration duration) {
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

  public void set(String key, Object value) {
    try {
      String jsonString = mapper.writeValueAsString(value);
      set(key, jsonString);
    } catch (JsonProcessingException ex) {
      log.error(ex.getMessage(), ex);
    }
  }

  public <T> T get(String key, Class<T> clazz) {
    try {
      String value = (String) get(key);
      return mapper.readValue(value, clazz);
    } catch (JsonProcessingException ex) {
      log.error(ex.getMessage(), ex);
    }
    return null;
  }

}
