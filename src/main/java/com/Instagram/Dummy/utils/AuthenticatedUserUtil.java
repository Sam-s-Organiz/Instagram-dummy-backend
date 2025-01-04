package com.Instagram.Dummy.utils;

import com.Instagram.Dummy.config.JwtUserDetails;
import com.Instagram.Dummy.modals.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedUserUtil {

    public static User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
        return jwtUserDetails.getUser();
    }
}
