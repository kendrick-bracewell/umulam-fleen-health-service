package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.constant.RoleType;
import com.umulam.fleen.health.constant.TokenType;
import com.umulam.fleen.health.exception.InvalidAuthenticationException;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.Role;
import com.umulam.fleen.health.model.dto.authentication.SignInDto;
import com.umulam.fleen.health.model.dto.authentication.SignUpDto;
import com.umulam.fleen.health.model.response.SignInResponse;
import com.umulam.fleen.health.model.security.UserDetailsImpl;
import com.umulam.fleen.health.service.AuthenticationService;
import com.umulam.fleen.health.service.CacheService;
import com.umulam.fleen.health.service.MemberService;
import com.umulam.fleen.health.service.RoleService;
import com.umulam.fleen.health.util.JwtProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

import static com.umulam.fleen.health.constant.AuthenticationConstant.AUTH_CACHE_PREFIX;
import static com.umulam.fleen.health.constant.AuthenticationConstant.AUTH_HEADER_PREFIX;
import static com.umulam.fleen.health.util.DateTimeUtil.toHours;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

  private final AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService;
  private final MemberService memberService;
  private final RoleService roleService;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;
  private final CacheService cacheService;

  public AuthenticationServiceImpl(AuthenticationManager authenticationManager,
                                   UserDetailsService userDetailsService,
                                   MemberService memberService,
                                   RoleService roleService,
                                   PasswordEncoder passwordEncoder,
                                   JwtProvider jwtProvider,
                                   CacheService cacheService) {
    this.authenticationManager = authenticationManager;
    this.userDetailsService = userDetailsService;
    this.memberService = memberService;
    this.roleService = roleService;
    this.passwordEncoder = passwordEncoder;
    this.jwtProvider = jwtProvider;
    this.cacheService = cacheService;
  }

  @Override
  public SignInResponse login(SignInDto dto) {
    Authentication authentication = authenticate(dto.getEmailAddress(), dto.getPassword());
    if (Objects.isNull(authentication)) {
      throw new InvalidAuthenticationException(dto.getEmailAddress());
    }

    UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
    String accessToken = createAccessToken(user);
    String refreshToken = createAccessToken(user);
    setContext(authentication);

    saveToken(user.getUsername(), accessToken);
    return new SignInResponse(accessToken, refreshToken);
  }

  @Override
  public SignInResponse signUp(SignUpDto dto) {
    Member member = dto.toMember();
    Role role = roleService.getRoleByCode(RoleType.USER.name());
    member.setPassword(passwordEncoder.encode(member.getPassword()));
    member.setRoles(Set.of(role));
    memberService.save(member);

    Authentication authentication = authenticate(dto.getEmailAddress(), dto.getPassword());
    setContext(authentication);

    UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
    String accessToken = createAccessToken(user);
    String refreshToken = createAccessToken(user);

    saveToken(user.getUsername(), accessToken);
    return new SignInResponse(accessToken, refreshToken);
  }

  @Override
  public void logout(String username) {
    String key = getAuthCacheKey(username);
    if (cacheService.exists(key)) {
      cacheService.delete(AUTH_CACHE_PREFIX.concat(username));
    }
  }

  @Override
  public String getLoggedInUserEmailAddress() {
    Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
    if (userDetails instanceof UserDetailsImpl) {
      return ((UserDetails) userDetails).getUsername();
    }
    return null;
  }

  @Override
  public Authentication authenticate(String emailAddress, String password) {
    UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(emailAddress, password);

    return authenticationManager.authenticate(authenticationToken);
  }

  @Override
  public void setContext(Authentication authentication) {
    if (authentication.isAuthenticated()) {
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
  }

  @Override
  public SignInResponse refreshToken(String token) {
    String username = null;
    try {
      username = jwtProvider.getUsernameFromToken(token);
      logout(username);
      UserDetailsImpl user = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

      String accessToken = createAccessToken(user);
      String refreshToken = createRefreshToken(user);
      saveToken(username, accessToken);
      return new SignInResponse(accessToken, refreshToken);
    } catch (ExpiredJwtException | MalformedJwtException | SignatureException ex) {
      log.error(ex.getMessage(), ex);
      throw new InvalidAuthenticationException(username);
    }
  }

  @Override
  public String createAccessToken(UserDetailsImpl user) {
    return jwtProvider.generateToken(user, TokenType.ACCESS_TOKEN);
  }

  @Override
  public String createRefreshToken(UserDetailsImpl user) {
    return jwtProvider.generateRefreshToken(user.getUsername(), TokenType.REFRESH_TOKEN);
  }

  @Override
  public void saveToken(String subject, String token) {
    Duration duration = Duration.ofHours(toHours(new Date(), jwtProvider.getExpirationDateFromToken(token)));
    cacheService.set(getAuthCacheKey(subject), token, duration);
  }

  public static String getAuthCacheKey(String username) {
    return AUTH_HEADER_PREFIX.concat(username);
  }
}
