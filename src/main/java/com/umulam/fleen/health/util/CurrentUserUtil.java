package com.umulam.fleen.health.util;

import com.umulam.fleen.health.model.security.FleenUser;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserUtil {

  public static FleenUser getCurrentUser() {
    SecurityContext context = SecurityContextHolder.getContext();
    Object principal = context.getAuthentication().getPrincipal();
    System.out.println(principal);
    if (principal instanceof UserDetails) {
        return (FleenUser) context.getAuthentication().getPrincipal();
    }
    else {
      return null;
    }
  }
}
