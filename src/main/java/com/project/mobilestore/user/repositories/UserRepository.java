package com.project.mobilestore.user.repositories;


import com.project.mobilestore.user.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUsernameOrEmail(String username, String email);

    UserEntity findFirstByUsername(String username);

    UserEntity findFirstByEmail(String email);
}
