package com.umulam.fleen.health.service.impl;

public interface AuthenticationService {

  String getLoggedInUserEmailAddress();

  void authenticateAfterSignUp(String username, String password);
}
