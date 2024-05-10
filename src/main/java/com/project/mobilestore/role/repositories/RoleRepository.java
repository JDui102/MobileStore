package com.project.mobilestore.role.repositories;

import com.project.mobilestore.role.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findFirstById(Long id);

    Role findByName(String Name);
}
