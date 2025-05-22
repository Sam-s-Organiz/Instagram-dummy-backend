package com.Instagram.Dummy.config;

import com.Instagram.Dummy.services.JWTservice;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JWTfilter extends OncePerRequestFilter {
  @Autowired @Lazy private JWTservice jwtService;

  @Autowired private UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    // Extract Authorization header from the request
    String authHeader = request.getHeader("Authorization");
    String token = null;
    String username = null;

    // Check if the header contains a valid Bearer token
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      token = authHeader.substring(7);
      username = jwtService.extractUserName(token);
    }

    // Proceed if the username is non-null and the context is unauthenticated
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

      // Load user details from the provided username
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

      // Validate the JWT token
      if (jwtService.validateToken(token, userDetails)) {

        // Create an authentication token for the SecurityContext
        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        // Set additional details for authentication
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Set the authentication token in the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }

    // Continue the filter chain
    filterChain.doFilter(request, response);
  }
}
