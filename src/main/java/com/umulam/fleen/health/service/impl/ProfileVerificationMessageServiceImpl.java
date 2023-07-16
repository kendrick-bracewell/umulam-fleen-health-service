package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.constant.verification.ProfileVerificationMessageType;
import com.umulam.fleen.health.exception.profileverificationmessage.ProfileVerificationMessageNotFoundException;
import com.umulam.fleen.health.model.domain.ProfileVerificationMessage;
import com.umulam.fleen.health.model.dto.profileverificationmessage.ProfileVerificationMessageDto;
import com.umulam.fleen.health.model.mapper.ProfileVerificationMessageMapper;
import com.umulam.fleen.health.model.request.search.ProfileVerificationMessageSearchRequest;
import com.umulam.fleen.health.model.response.other.DeleteIdsDto;
import com.umulam.fleen.health.model.response.profileverificationmessage.GetProfileVerificationMessageId;
import com.umulam.fleen.health.model.response.profileverificationmessage.GetProfileVerificationMessages;
import com.umulam.fleen.health.model.response.profileverificationmessage.GetProfileVerificationMessagesBasic;
import com.umulam.fleen.health.model.view.ProfileVerificationMessageView;
import com.umulam.fleen.health.model.view.SearchResultView;
import com.umulam.fleen.health.repository.jpa.ProfileVerificationMessageJpaRepository;
import com.umulam.fleen.health.service.ProfileVerificationMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.umulam.fleen.health.constant.authentication.AuthenticationConstant.PROFILE_VERIFICATION_MESSAGE_TEMPLATE_CACHE_PREFIX;
import static com.umulam.fleen.health.util.FleenHealthUtil.areNotEmpty;
import static com.umulam.fleen.health.util.FleenHealthUtil.toSearchResult;
import static java.util.Objects.nonNull;

@Slf4j
@Service
public class ProfileVerificationMessageServiceImpl implements ProfileVerificationMessageService {

  private final CacheService cacheService;
  private final ProfileVerificationMessageJpaRepository repository;

  public ProfileVerificationMessageServiceImpl(
          CacheService cacheService,
          ProfileVerificationMessageJpaRepository repository) {
    this.cacheService = cacheService;
    this.repository = repository;
  }

  @Override
  public ProfileVerificationMessage getProfileVerificationMessageByType(@NonNull ProfileVerificationMessageType messageType) {
    return repository
            .findByVerificationMessageType(messageType)
            .orElse(null);
  }

  @Override
  public ProfileVerificationMessageView getById(Integer profileVerificationMessageId) {
    Optional<ProfileVerificationMessage> messageExists = repository.findById(profileVerificationMessageId);
    if (messageExists.isEmpty()) {
      throw new ProfileVerificationMessageNotFoundException(profileVerificationMessageId);
    }
    return ProfileVerificationMessageMapper.toProfileVerificationMessageView(messageExists.get());
  }

  @Override
  @Transactional(readOnly = true)
  public SearchResultView findProfileVerificationMessages(ProfileVerificationMessageSearchRequest req) {
    Page<GetProfileVerificationMessages> page;

    if (areNotEmpty(req.getStartDate(), req.getEndDate())) {
      page = repository.findByDateBetween(req.getStartDate().atStartOfDay(), req.getEndDate().atStartOfDay(), req.getPage());
    } else if (nonNull(req.getVerificationMessageType())) {
      page = repository.findByVerificationMessageType(req.getVerificationMessageType(), req.getPage());
    } else {
      page = repository.findAllBasic(req.getPage());
    }

    return toSearchResult(page.getContent(), page);
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
    messages
            .stream()
            .filter(Objects::nonNull)
            .forEach(message -> saveProfileVerificationVerificationMessageToCache(message.getId(), message));
  }

  public List<ProfileVerificationMessage> getMessagesForCache() {
    return repository.findAll();
  }

  @Override
  public void saveProfileVerificationVerificationMessageToCache(Integer messageId, ProfileVerificationMessage verificationMessage) {
    String key = getProfileVerificationMessageCacheKey(messageId);
    cacheService.set(key, verificationMessage);
  }

  @Override
  public ProfileVerificationMessage getProfileVerificationMessageFromCache(Integer messageId) {
    String key = getProfileVerificationMessageCacheKey(messageId);
    return cacheService.get(key, ProfileVerificationMessage.class);
  }

  @Override
  public List<GetProfileVerificationMessagesBasic> getBasicDetails() {
    return repository.getBasicDetails();
  }

  @Override
  @Transactional
  public void saveProfileVerificationMessage(ProfileVerificationMessageDto dto) {
    ProfileVerificationMessage verificationMessage = dto.toProfileVerificationMessage();
    repository.save(verificationMessage);
  }

  @Override
  @Transactional
  public void updateProfileVerificationMessage(Integer id, ProfileVerificationMessageDto dto) {
    GetProfileVerificationMessageId verificationMessageId = repository.getId(id);
    if (Objects.isNull(verificationMessageId)) {
      throw new ProfileVerificationMessageNotFoundException(id);
    }

    ProfileVerificationMessage verificationMessage = dto.toProfileVerificationMessage();
    verificationMessage.setId(id);
    repository.save(verificationMessage);
  }

  @Override
  @Transactional
  public void deleteMany(DeleteIdsDto dto) {
    List<ProfileVerificationMessage> messages = dto
            .getIds()
            .stream()
            .map(id -> ProfileVerificationMessage.builder()
                    .id(id).build())
            .collect(Collectors.toList());

    repository.deleteAll(messages);
  }
}
