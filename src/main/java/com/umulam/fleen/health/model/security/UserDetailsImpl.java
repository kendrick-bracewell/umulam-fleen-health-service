package com.umulam.fleen.health.model.security;

import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.Role;
import com.umulam.fleen.health.model.dto.authentication.JwtTokenDetails;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserDetailsImpl implements UserDetails {

  private static final long serialVersionUID = 1L;

  private long id;
  private String emailAddress;
  private String password;
  private Set<Role> roles;
  private Collection<? extends GrantedAuthority> authorities;
  private String fullName;
  private String avatar;

  public UserDetailsImpl(Integer id, String emailAddress, String password,
                         Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.emailAddress = emailAddress;
    this.password = password;
    this.authorities = authorities;
  }

  public static UserDetailsImpl build(Member member) {
    List<GrantedAuthority> authorities = member.getRoles()
            .stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_".concat(role.getCode())))
            .collect(Collectors.toList());

    var details = new UserDetailsImpl(member.getId(), member.getEmailAddress(), member.getPassword(), authorities);
    details.setRoles(member.getRoles());
    details.setFullName((member.getFirstName().concat(" ").concat(member.getLastName())));
    details.setAvatar(member.getProfilePhoto());
    return details;
  }

  public static UserDetailsImpl buildFromTokenDetails(JwtTokenDetails tokenDetails) {
    List<GrantedAuthority> authorities = Arrays.stream(tokenDetails.getRoles())
            .map(role -> new SimpleGrantedAuthority("ROLE_".concat(role)))
            .collect(Collectors.toList());

    var details = new UserDetailsImpl((Integer) tokenDetails.getMemberId(), tokenDetails.getSub(), null, authorities);
    var roles = Arrays.stream(tokenDetails.getRoles())
                    .map((role) -> Role.builder().code(role).build())
                    .collect(Collectors.toSet());

    details.setRoles(roles);
    return details;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

  @Override
  public String getUsername() {
        return emailAddress;
    }

  @Override
  public boolean isAccountNonExpired() {
        return true;
    }

  @Override
  public boolean isAccountNonLocked() {
        return true;
    }

  @Override
  public boolean isCredentialsNonExpired() {
        return true;
    }

  @Override
  public boolean isEnabled() {
        return true;
    }
}
