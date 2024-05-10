package com.project.mobilestore.controller;

import com.project.mobilestore.user.controllers.AuthController;
import com.project.mobilestore.user.dtos.RegistrationDTO;
import com.project.mobilestore.user.services.UserService;
import com.project.mobilestore.config.security.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private AuthController authController;

    private RegistrationDTO registrationDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        registrationDTO = RegistrationDTO.builder()
                .username("testUser")
                .email("test@example.com")
                .password("testPassword")
                .build();
    }

    @Test
    void getRegisterForm() {
        String viewName = authController.getRegisterForm(model);
        assertEquals("auth/register", viewName);
        verify(model, times(1)).addAttribute(eq("user"), any(RegistrationDTO.class));
    }

    @Test
    void register_UserExists() {
        when(userService.existsByUsernameOrEmail(any(), any())).thenReturn(true);

        String viewName = authController.register(registrationDTO, bindingResult, model);

        assertEquals("redirect:/register?fail", viewName);
        verify(userService, never()).saveUser(any());
    }

    @Test
    void register_ValidationError() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = authController.register(registrationDTO, bindingResult, model);

        assertEquals("auth/register", viewName);
        verify(userService, never()).saveUser(any());
    }

    @Test
    void register_Success() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.existsByUsernameOrEmail(any(), any())).thenReturn(false);

        String viewName = authController.register(registrationDTO, bindingResult, model);

        assertEquals("redirect:/login", viewName);
        verify(userService, times(1)).saveUser(registrationDTO);
    }
}
