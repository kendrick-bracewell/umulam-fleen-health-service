package com.umulam.fleen.health.service;

import com.umulam.fleen.health.constant.verification.ProfileVerificationMessageType;
import com.umulam.fleen.health.model.domain.ProfileVerificationMessage;
import com.umulam.fleen.health.model.dto.profileverificationmessage.ProfileVerificationMessageDto;
import com.umulam.fleen.health.model.request.search.ProfileVerificationMessageSearchRequest;
import com.umulam.fleen.health.model.response.other.DeleteIdsDto;
import com.umulam.fleen.health.model.response.profileverificationmessage.GetProfileVerificationMessagesBasic;
import com.umulam.fleen.health.model.view.ProfileVerificationMessageView;
import com.umulam.fleen.health.model.view.search.SearchResultView;

import java.util.List;

public interface ProfileVerificationMessageService {

  ProfileVerificationMessage getProfileVerificationMessageByType(ProfileVerificationMessageType messageType);

  boolean existsById(Long id);

  void saveProfileVerificationVerificationMessageToCache(Long messageId, ProfileVerificationMessage profileVerificationMessage);

  ProfileVerificationMessage getProfileVerificationMessageFromCache(Long messageId);

  List<GetProfileVerificationMessagesBasic> getBasicDetails();

  void saveProfileVerificationMessage(ProfileVerificationMessageDto dto);

  void updateProfileVerificationMessage(Long id, ProfileVerificationMessageDto dto);

  void deleteMany(DeleteIdsDto dto);

  SearchResultView findProfileVerificationMessages(ProfileVerificationMessageSearchRequest searchRequest);

  ProfileVerificationMessageView getById(Long profileVerificationMessageId);
}
