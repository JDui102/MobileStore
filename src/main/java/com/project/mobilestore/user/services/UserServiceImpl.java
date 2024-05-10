package com.project.mobilestore.user.services;

import com.project.mobilestore.role.repositories.RoleRepository;
import com.project.mobilestore.user.dtos.RegistrationDTO;
import com.project.mobilestore.user.entities.UserEntity;
import com.project.mobilestore.user.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final MessageSource messageSource;

    @Autowired
    private final ModelMapper mapper;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, MessageSource messageSource, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.messageSource = messageSource;
        this.mapper = mapper;
    }

    @Value("${role.default.id}")
    private Long roleDefaultId;

    // Get message form message.properties
    private String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    @Override
    public UserEntity saveUser(RegistrationDTO registrationDTO) {
        UserEntity user = mapper.map(registrationDTO, UserEntity.class);

        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setRole(roleRepository.findFirstById(roleDefaultId)); // Member role default
        return userRepository.save(user);
    }

    @Override
    public UserEntity findFirstByEmail(String email) {
        return userRepository.findFirstByEmail(email);
    }

    @Override
    public Boolean existsByUsernameOrEmail(String username, String email) {
        return userRepository.existsByUsernameOrEmail(username, email);
    }

    @Override
    public UserEntity findFirstByUsername(String username) {
        return userRepository.findFirstByUsername(username);
    }
}
