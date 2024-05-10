package com.project.mobilestore.user.services;

import com.project.mobilestore.user.dtos.RegistrationDTO;
import com.project.mobilestore.user.entities.UserEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserEntity saveUser(RegistrationDTO registrationDto);

    UserEntity findFirstByUsername(String username);

    UserEntity findFirstByEmail(String email);

    Boolean existsByUsernameOrEmail(String username, String email);
}
