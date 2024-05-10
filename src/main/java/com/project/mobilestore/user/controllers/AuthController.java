package com.project.mobilestore.user.controllers;

import com.project.mobilestore.user.dtos.RegistrationDTO;
import com.project.mobilestore.user.services.UserService;
import com.project.mobilestore.config.security.CustomUserDetailsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class AuthController {
    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;

    @GetMapping("/login")
    public String loginPage(){
        return "auth/login";
    }

    @GetMapping("/register")
    public String getRegisterForm(Model model) {
        model.addAttribute("user", RegistrationDTO.builder().build());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user")RegistrationDTO user,
                           BindingResult result, Model model) {
        // Check if username or email already exists?
        if(userService.existsByUsernameOrEmail(user.getUsername(), user.getEmail())) {
            return "redirect:/register?fail";
        }

        // Check validate
        if(result.hasErrors()) {
            model.addAttribute("user", user);
            return "auth/register";
        }

        userService.saveUser(user);

        return "redirect:/login";
    }
}