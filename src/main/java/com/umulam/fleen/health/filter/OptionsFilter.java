package com.umulam.fleen.health.filter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.umulam.fleen.health.filter.SimpleCorsFilter.setHeaders;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class OptionsFilter extends OncePerRequestFilter {

  private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
  private static final String URL_PATTERN = "/api/**";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain)  throws ServletException, IOException {
    if (HttpMethod.OPTIONS.name().equals(request.getMethod())
      && PATH_MATCHER.match(URL_PATTERN, request.getRequestURI())) {
      setHeaders(response);
      response.setStatus(HttpServletResponse.SC_OK);
      return;
    }

    filterChain.doFilter(request, response);
  }
}
