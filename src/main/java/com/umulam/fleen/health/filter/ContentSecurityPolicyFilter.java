package com.umulam.fleen.health.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class ContentSecurityPolicyFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

    // The sha256-hashes listed in the csPolicy string below is to allow the inline execution of Swagger or application UI related scripts or stylesheets.
  String csPolicy = "default-src 'self';" +
            "img-src 'self' data: blob:;" +
            "font-src 'self';" +
            "object-src 'self';" +
            "media-src 'self';" +
            "child-src 'self';" +
            "script-src 'self' 'sha256-pyVPiLlnqL9OWVoJPs/E6VVF5hBecRzM2gBiarnaqAo=';" +
            "style-src 'self' 'sha256-pyVPiLlnqL9OWVoJPs/E6VVF5hBecRzM2gBiarnaqAo=';" +
            "script-src-elem 'self' 'sha256-epfHad2QZXZLgp6wYaCqbNFkXh59Q4UDNhZGymAervc='";

    response.setHeader("X-Frame-Options", "deny");
    response.setHeader("Strict-Transport-Security", "max-age=63072000; includeSubDomains; preload");
    response.setHeader("Content-Security-Policy", csPolicy);
    response.setHeader("X-Content-Type-Options", "nosniff");
    filterChain.doFilter(request, response);
  }
}
