package com.umulam.fleen.health.service;

import com.umulam.fleen.health.constant.verification.ProfileVerificationMessageType;
import com.umulam.fleen.health.model.domain.ProfileVerificationMessage;

public interface ProfileVerificationMessageService {

  ProfileVerificationMessage getProfileVerificationMessageByType(ProfileVerificationMessageType messageType);
}
