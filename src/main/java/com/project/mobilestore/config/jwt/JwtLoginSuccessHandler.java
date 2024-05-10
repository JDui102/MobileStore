package com.project.mobilestore.config.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.project.mobilestore.config.jwt.JwtConstants.SEVEN_DAYS;

@Component
@AllArgsConstructor
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        // Retrieve UserDetails from the authenticated Authentication object
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Generate a JWT token using UserDetails
        String token = jwtUtil.generateToken(userDetails);

        // Create a new cookie with the JWT token
        Cookie cookie = new Cookie("token", token);
        // Devided 100 for convert mili second to second
        cookie.setMaxAge((int) (System.currentTimeMillis() + SEVEN_DAYS / 1000));
        // Set cookie expiration time to maximum value
        cookie.setPath("/"); // Set the path of the cookie to "/"
        response.addCookie(cookie); // Add the cookie to the response

        // Redirect the user to the "/categories" page after successful authentication
        response.sendRedirect("/categories");
    }
}
