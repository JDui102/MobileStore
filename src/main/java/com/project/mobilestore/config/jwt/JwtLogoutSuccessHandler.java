package com.project.mobilestore.config.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
    private final JwtUtil jwtUtil;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals("token")) {
//                    // Extract token from the cookie
//                    String token = cookie.getValue();
//
//                    cookie = new Cookie("token", jwtUtil.invalidateToken(token));
//
//                    response.addCookie(cookie);
//                    break;
//                }
//            }
//        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("token")) {
                    cookies[i].setMaxAge(0);
                    response.addCookie(cookies[i]);
                }
            }
        }

        setDefaultTargetUrl("/login?logout");

        super.onLogoutSuccess(request, response, authentication);
    }
}
