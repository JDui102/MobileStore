package com.project.mobilestore.service;

import com.project.mobilestore.role.entities.Role;
import com.project.mobilestore.role.repositories.RoleRepository;
import com.project.mobilestore.user.dtos.RegistrationDTO;
import com.project.mobilestore.user.entities.UserEntity;
import com.project.mobilestore.user.repositories.UserRepository;
import com.project.mobilestore.user.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MessageSource messageSource;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private UserServiceImpl userService;

    private RegistrationDTO registrationDTO;

    private UserEntity userEntity;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserServiceImpl(userRepository, roleRepository, passwordEncoder, messageSource, mapper);

        registrationDTO = RegistrationDTO.builder()
                .username("testUser")
                .email("test@example.com")
                .password("testPassword")
                .build();

        userEntity = new UserEntity();
    }

    @Test
    public void testSaveUser_Success() {
        userEntity = new UserEntity();
        when(mapper.map(any(), eq(UserEntity.class))).thenReturn(userEntity);
        when(roleRepository.findFirstById(any())).thenReturn(new Role());

        userService.saveUser(registrationDTO);

        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    public void testFindFirstByEmail_Success() {
        when(userRepository.findFirstByEmail("test@example.com")).thenReturn(userEntity);

        UserEntity result = userService.findFirstByEmail("test@example.com");

        assertNotNull(result);
    }

    @Test
    public void testFindFirstByEmail_NotFound() {
        when(userRepository.findFirstByEmail("test@example.com")).thenReturn(null);

        UserEntity result = userService.findFirstByEmail("test@example.com");

        assertNull(result);
    }

    @Test
    public void testExistsByUsernameOrEmail_True() {
        when(userRepository.existsByUsernameOrEmail("testUser", "test@example.com")).thenReturn(true);

        assertTrue(userService.existsByUsernameOrEmail("testUser", "test@example.com"));
    }

    @Test
    public void testExistsByUsernameOrEmail_False() {
        when(userRepository.existsByUsernameOrEmail("testUser", "test@example.com")).thenReturn(false);

        assertFalse(userService.existsByUsernameOrEmail("testUser", "test@example.com"));
    }

    @Test
    public void testFindFirstByUsername_Success() {
        when(userRepository.findFirstByUsername("testUser")).thenReturn(userEntity);

        UserEntity result = userService.findFirstByUsername("testUser");

        assertNotNull(result);
    }

    @Test
    public void testFindFirstByUsername_NotFound() {
        when(userRepository.findFirstByUsername("testUser")).thenReturn(null);

        UserEntity result = userService.findFirstByUsername("testUser");

        assertNull(result);
    }
}
