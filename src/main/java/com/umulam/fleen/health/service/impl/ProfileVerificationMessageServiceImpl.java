package com.umulam.fleen.health.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umulam.fleen.health.constant.verification.ProfileVerificationMessageType;
import com.umulam.fleen.health.model.domain.ProfileVerificationMessage;
import com.umulam.fleen.health.repository.jpa.ProfileVerificationMessageRepository;
import com.umulam.fleen.health.service.ProfileVerificationMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.util.List;
import java.util.Objects;

import static com.umulam.fleen.health.constant.authentication.AuthenticationConstant.PROFILE_VERIFICATION_MESSAGE_TEMPLATE_CACHE_PREFIX;

@Slf4j
@Service
public class ProfileVerificationMessageServiceImpl implements ProfileVerificationMessageService {

  private final CacheService cacheService;
  private final ProfileVerificationMessageRepository repository;
  private final ObjectMapper mapper;

  public ProfileVerificationMessageServiceImpl(
          CacheService cacheService,
          ProfileVerificationMessageRepository repository,
          ObjectMapper mapper) {
    this.cacheService = cacheService;
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public ProfileVerificationMessage getProfileVerificationMessageByType(@NonNull ProfileVerificationMessageType messageType) {
    return repository
            .findByVerificationMessageType(messageType)
            .orElse(null);
  }

  @Override
  public boolean existsById(Integer id) {
    return repository.existsById(id);
  }

  private String getProfileVerificationMessageCacheKey(Integer messageId) {
    return PROFILE_VERIFICATION_MESSAGE_TEMPLATE_CACHE_PREFIX.concat(String.valueOf(messageId));
  }

  @EventListener(ApplicationReadyEvent.class)
  public void saveMessagesToCacheOnStartup() {
    List<ProfileVerificationMessage> messages = getMessagesForCache();
    saveMessagesToCache(messages);
  }

  @Scheduled(cron = "0 0 */3 * * *")
  public void saveMessagesToCache() {
    saveMessagesToCache(null);
  }

  public void saveMessagesToCache(List<ProfileVerificationMessage> messages) {
    if (Objects.isNull(messages) || messages.isEmpty()) {
      messages = getMessagesForCache();
    }
    messages.forEach(message -> saveProfileVerificationVerificationMessageToCache(message.getId(), message));
  }

  public List<ProfileVerificationMessage> getMessagesForCache() {
    return repository.findAll();
  }

  @Override
  public void saveProfileVerificationVerificationMessageToCache(Integer messageId, ProfileVerificationMessage profileVerificationMessage) {
    try {
      String value = mapper.writeValueAsString(profileVerificationMessage);
      String key = getProfileVerificationMessageCacheKey(messageId);
      cacheService.set(key, value);
    } catch (JsonProcessingException ex) {
      log.error(ex.getMessage(), ex);
    }
  }

  @Override
  public ProfileVerificationMessage getProfileVerificationMessageFromCache(Integer messageId) {
    String key = getProfileVerificationMessageCacheKey(messageId);
    Object value = cacheService.get(key);
    return mapper.convertValue(value, ProfileVerificationMessage.class);
  }
}
