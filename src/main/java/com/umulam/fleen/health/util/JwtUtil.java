package com.umulam.fleen.health.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umulam.fleen.health.model.domain.Role;
import com.umulam.fleen.health.model.dto.authentication.JwtTokenDetails;
import com.umulam.fleen.health.model.security.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.umulam.fleen.health.util.DateTimeUtil.getTimeInMillis;

@Slf4j
@Component
@Getter
@Setter
@PropertySource("classpath:application.properties")
public class JwtUtil {

  public static final long JWT_TOKEN_VALIDITY = getTimeInMillis(60, 60, 5, 0);
  public static final long REFRESH_TOKEN_VALIDITY = getTimeInMillis(60, 60, 24, 2);

  @Value("${umulam.fleen.health.jwt.secret}")
  private String secret;

  public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

  public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }

  public Map<String, Object> getTokenDetails(String token) {
    return new HashMap<>(getAllClaimsFromToken(token));
  }

  public JwtTokenDetails getDetails(String token) {
    ObjectMapper mapper = new ObjectMapper();
    JwtTokenDetails details = mapper.convertValue(getTokenDetails(token), JwtTokenDetails.class);
    return details;
  }

  public Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  public String generateToken(UserDetails details) {
    Map<String, Object> claims = new HashMap<>();
    UserDetailsImpl userDetails = (UserDetailsImpl) details;

    String[] roles = userDetails.getRoles()
                      .stream()
                      .map(Role::getCode)
                      .toArray(String[]::new);

    claims.put("memberId", userDetails.getId());
    claims.put("roles", roles);
    claims.put("fullName", userDetails.getFullName());
    claims.put("emailAddress", userDetails.getEmailAddress());
    claims.put("avatar", userDetails.getAvatar());
    return generateToken(claims, userDetails.getUsername());
  }

  private String generateToken(Map<String, Object> claims, String subject) {

    return Jwts.builder()
              .setClaims(claims)
              .setSubject(subject)
              .setIssuedAt(new Date(System.currentTimeMillis()))
              .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
              .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

  public String generateRefreshToken(String subject) {
    return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

  public Boolean validateToken(String token, UserDetails details) {
    final String username = getUsernameFromToken(token);
    return (username.equals(details.getUsername()) && !isTokenExpired(token));
  }

  public Boolean checkToken(String token, UserDetails details) {
    final String username = getUsernameFromToken(token);
    return (username.equals(details.getUsername()));
  }

  private void setMemberType(UserDetails userDetails, Map<String, Object> claimsMap) {
    UserDetailsImpl details = (UserDetailsImpl) userDetails;
    String memberType = null;
    for (Role role : details.getRoles()) {
          if (role.getCode().equals("SuperAdministrator")) {
              memberType = role.getCode();
              break;
          }
          else if (role.getCode().equals("Administrator")) {
            memberType = role.getCode();
            break;
          }
          else if (role.getCode().equals("Member")) {
            memberType = role.getCode();
            break;
          }
      }
    claimsMap.put("memberType", memberType);
  }

}
