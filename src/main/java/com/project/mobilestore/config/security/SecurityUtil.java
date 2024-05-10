package com.project.mobilestore.config.security;

import com.project.mobilestore.user.entities.UserEntity;
import com.project.mobilestore.user.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SecurityUtil {
    private final UserService userService;

    // Static method to retrieve the username of the currently authenticated user
    public static String getSessionUser() {
        // Retrieve the Authentication object from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is not null and is not an instance of AnonymousAuthenticationToken
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            // Return the username of the authenticated user
            return authentication.getName();
        }

        // If authentication is null or is an instance of AnonymousAuthenticationToken, return null
        return null;
    }

    // Get user information
    public UserEntity getUserInfo() {
        UserEntity user = new UserEntity();
        String username = SecurityUtil.getSessionUser();
        if(username != null)
            user = userService.findFirstByUsername(username);

        return user;
    }
}
