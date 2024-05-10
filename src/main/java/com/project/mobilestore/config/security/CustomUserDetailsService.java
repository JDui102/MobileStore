package com.project.mobilestore.config.security;

import com.project.mobilestore.user.entities.UserEntity;
import com.project.mobilestore.user.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find the user entity by username from the repository
        UserEntity user = userRepository.findFirstByUsername(username);

        // Check if user is found
        if(user != null) {
            // Create a GrantedAuthority from the user's role
            GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName());

            // Create and return a UserDetails object representing the authenticated user
            return new User(
                    user.getUsername(),     // Username
                    user.getPassword(),     // Password
                    Collections.singletonList(authority)  // Granted authority
            );
        } else { // If user is not found, throw UsernameNotFoundException
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }
}
