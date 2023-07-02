package com.umulam.fleen.health.filter;

import com.umulam.fleen.health.constant.authentication.TokenType;
import com.umulam.fleen.health.model.dto.authentication.JwtTokenDetails;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.service.CacheService;
import com.umulam.fleen.health.util.JwtProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.umulam.fleen.health.constant.authentication.AuthenticationConstant.AUTH_HEADER_PREFIX;
import static com.umulam.fleen.health.constant.authentication.AuthenticationConstant.TOKEN_TYPE_KEY;
import static com.umulam.fleen.health.service.impl.AuthenticationServiceImpl.getAuthCacheKey;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtProvider jwtProvider;
  private final CacheService cacheService;
  private final HandlerExceptionResolver resolver;

  public JwtAuthenticationFilter(JwtProvider jwtProvider,
                                 CacheService cacheService,
                                 @Lazy @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
    this.jwtProvider = jwtProvider;
    this.cacheService = cacheService;
    this.resolver = handlerExceptionResolver;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain)
            throws ServletException, IOException {
    final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (header == null || !StringUtils.startsWithIgnoreCase(header, AUTH_HEADER_PREFIX)) {
        filterChain.doFilter(request, response);
        return;
    }

    int index = AUTH_HEADER_PREFIX.length();
    final String token = header.substring(index);

    String emailAddress = null;
    try {
      emailAddress = jwtProvider.getUsernameFromToken(token);
    } catch (IllegalArgumentException | ExpiredJwtException | MalformedJwtException | SignatureException ex) {
      log.error(ex.getMessage(), ex);
      resolver.resolveException(request, response, null, ex);
      return;
    }

    if (!StringUtils.isNotEmpty(emailAddress)) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      if (SecurityContextHolder.getContext().getAuthentication() == null) {
        JwtTokenDetails details = jwtProvider.getBasicDetails(token);
        UserDetails userDetails = FleenUser.fromToken(details);
        String key = getAuthCacheKey(details.getSub());
        String savedToken = (String) cacheService.get(key);

        if (jwtProvider.isTokenValid(token, userDetails)) {
          if (cacheService.exists(key) && savedToken != null) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
          }
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }

    filterChain.doFilter(request, response);
  }
}
