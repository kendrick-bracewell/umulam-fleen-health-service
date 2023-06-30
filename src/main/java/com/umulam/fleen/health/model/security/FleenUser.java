package com.umulam.fleen.health.model.security;

import com.umulam.fleen.health.constant.authentication.MfaType;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.Role;
import com.umulam.fleen.health.model.dto.authentication.JwtTokenDetails;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.umulam.fleen.health.util.FleenAuthorities.buildAuthorities;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class FleenUser implements UserDetails {

  private static final long serialVersionUID = 1L;

  private Integer id;
  private String emailAddress;
  private String phoneNumber;
  private String password;
  private Collection<? extends GrantedAuthority> authorities;
  private String fullName;
  private String profilePhoto;
  private Set<Role> roles;
  private String status;
  private boolean mfaEnabled;
  private MfaType mfaType;

  public FleenUser(Integer id, String emailAddress, String password,
                   Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.emailAddress = emailAddress;
    this.password = password;
    this.authorities = authorities;
  }

  public static FleenUser fromMember(Member member) {
    List<String> roles = member.getRoles().stream().map(Role::getCode).collect(Collectors.toList());
    List<GrantedAuthority> authorities = buildAuthorities(roles);

    var user = FleenUser.builder()
            .id(member.getId())
            .emailAddress(member.getEmailAddress())
            .phoneNumber(member.getPhoneNumber())
            .password(member.getPassword())
            .authorities(authorities)
            .status(member.getMemberStatus().getCode())
            .build();

    String fullName = member.getFirstName() + " " + member.getLastName();
    user.setFullName(fullName);
    user.setProfilePhoto(member.getProfilePhoto());
    user.setRoles(member.getRoles());
    user.setMfaEnabled(member.isMfaEnabled());
    user.setMfaType(member.getMfaType());
    return user;
  }

  public static FleenUser fromToken(JwtTokenDetails details) {
    List<GrantedAuthority> authorities = buildAuthorities(Arrays.asList(details.getAuthorities()));
    return new FleenUser(details.getUserId(), details.getSub(), null, authorities);
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

  public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
    this.authorities = authorities;
  }
}
