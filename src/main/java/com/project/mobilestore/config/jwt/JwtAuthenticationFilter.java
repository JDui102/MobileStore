package com.project.mobilestore.config.jwt;

import java.io.IOException;

import com.project.mobilestore.config.security.CustomUserDetailsService;
import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private JwtUtil jwtUtil;
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Get the token from the cookie
        String token = getTokenFromCookie(request, response);

        // If token is null or empty, allow the request to continue its navigation
        if (token == null || token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the username from the token
        String userName = getUserNameFromToken(token);

        // If username is null or token is not valid, allow the request to continue its navigation
        if (userName == null || !jwtUtil.validateToken(token, userDetailsService.loadUserByUsername(userName))) {
            filterChain.doFilter(request, response);
            return;
        }

        // If authentication object doesn't exist, create a new one and set it into SecurityContextHolder
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // Allow the request to continue its navigation
        filterChain.doFilter(request, response);
    }

    private String getTokenFromCookie(HttpServletRequest request, HttpServletResponse response) {
        // Define the name of the token cookie
        String tokenCookieName = "token";

        // Initialize variables
        String token = null;

        // Retrieve all cookies from the request
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            // Iterate through the cookies to find the one with the token cookie name
            for (Cookie cookie : cookies) {
                if (tokenCookieName.equals(cookie.getName())) {
                    // If found, retrieve the token value and break the loop
                    token = cookie.getValue();
                    break;
                }
            }
        }

        return token;
    }

    private String getUserNameFromToken(String token) {
        // Define the prefix for a JWT token in the request header
        String requestTokenHeader = "Bearer " + token;

        // Initialize variables
        String username = null;
        String jwtToken = null;

        // Check if the token is not null, not empty, and starts with the Bearer prefix
        if (token != null && !token.isEmpty() && requestTokenHeader.startsWith("Bearer ")) {
            // Extract the JWT token from the request header
            jwtToken = requestTokenHeader.substring(7);
            try {
                // Extract the username from the JWT token using the JWT service
                username = jwtUtil.extractUsername(jwtToken);
            } catch (Exception e) {
                // Handle any exceptions that occur during username extraction
                e.printStackTrace();
            }
        }

        return username;
    }
}