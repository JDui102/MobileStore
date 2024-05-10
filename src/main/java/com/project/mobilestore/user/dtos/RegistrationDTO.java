package com.project.mobilestore.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class RegistrationDTO {
    @NotEmpty(message = "Username can't be left empty!")
    private String username;

    @Email(message = "Invalid email!")
    @NotEmpty(message = "Email can't be left empty!")
    private String email;

    @NotEmpty(message = "Password can't be left empty!")
    private String password;
}
