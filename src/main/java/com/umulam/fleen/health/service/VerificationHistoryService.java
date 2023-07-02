package com.umulam.fleen.health.service;

import com.umulam.fleen.health.model.domain.ProfileVerificationHistory;

public interface VerificationHistoryService {

  ProfileVerificationHistory saveVerificationHistory(ProfileVerificationHistory history);
}
