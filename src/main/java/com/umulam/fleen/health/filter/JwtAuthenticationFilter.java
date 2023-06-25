package com.umulam.fleen.health.filter;

import com.umulam.fleen.health.model.security.UserDetailsImpl;
import com.umulam.fleen.health.util.JwtUtil;
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

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtUtil jwtUtil;
  private final HandlerExceptionResolver resolver;

  public JwtAuthenticationFilter(JwtUtil jwtUtil,
                                 @Lazy @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
    this.jwtUtil = jwtUtil;
    this.resolver = handlerExceptionResolver;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain)
            throws ServletException, IOException {

    final String tokenHeader = request.getHeader("Authorization");
    final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (header == null || !StringUtils.startsWith(tokenHeader,"Bearer ")) {
        filterChain.doFilter(request, response);
        return;
    }

    System.out.println("header " + header);

    final String token = header.substring(7);
    String emailAddress = null;

    try {
      emailAddress = jwtUtil.getUsernameFromToken(token);
    } catch (IllegalArgumentException e) {
        System.out.println("Unable to get JWT Token");
    } catch (ExpiredJwtException | MalformedJwtException | SignatureException e) {
        resolver.resolveException(request, response, null, e);
        return;
    }

    if (!StringUtils.isNotEmpty(emailAddress)) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = UserDetailsImpl.buildFromTokenDetails(jwtUtil.getDetails(token));

          if (jwtUtil.validateToken(token, userDetails)) {
              UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                                    null,
                                    userDetails.getAuthorities());
              authentication
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
              SecurityContextHolder.getContext().setAuthentication(authentication);
            }
          }
        } catch (Exception e) {
          logger.error("Cannot set user authentication: {}", e);
        }

    filterChain.doFilter(request, response);
  }
}
