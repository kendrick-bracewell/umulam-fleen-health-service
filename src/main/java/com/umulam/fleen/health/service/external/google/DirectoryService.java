package com.umulam.fleen.health.service.external.google;

import com.google.api.services.admin.directory.Directory;
import com.google.api.services.admin.directory.model.Alias;
import com.google.api.services.admin.directory.model.User;
import com.google.api.services.admin.directory.model.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class DirectoryService {
  private final Directory service;
  private static final String domainName = "volunux.com";

  public DirectoryService(Directory service) {
    this.service = service;
  }

  public Object createUser(@NonNull String userEmail) {
    try {
      User user = new User();
      user.setPrimaryEmail(userEmail);
      user.setPassword("78789898");
      user.setChangePasswordAtNextLogin(true);
      return service
              .users()
              .insert(user);
    } catch (IOException ex) {
      log.error(ex.getMessage(), ex);
    }
    return null;
  }

  public Object createEmailAlias(@NonNull String userEmail, String firstName, String lastName) {
    String emailAlias = firstName.concat(".").concat(lastName).concat("@").concat(domainName).toLowerCase();
    System.out.println(emailAlias);
    try {
      Alias alias = new Alias().setAlias(emailAlias);
      return service
              .users()
              .aliases()
              .insert(userEmail, alias)
              .execute();
    } catch (IOException ex) {
      log.error(ex.getMessage(), ex);
    }
    return null;
  }

}
