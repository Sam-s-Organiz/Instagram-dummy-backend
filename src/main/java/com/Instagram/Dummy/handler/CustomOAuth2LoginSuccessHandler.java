package com.Instagram.Dummy.handler;

 import com.Instagram.Dummy.services.JWTservice;
 import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTservice jwtService;

    public CustomOAuth2LoginSuccessHandler(JWTservice jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        String email = authToken.getPrincipal().getAttribute("email");

        // Generate JWT
        String jwtToken = jwtService.generateToken(email);

        // Respond with the JWT
        response.setContentType("application/json");
        response.getWriter().write("{\"token\":\"" + jwtToken + "\"}");
        response.getWriter().flush();
    }
}
