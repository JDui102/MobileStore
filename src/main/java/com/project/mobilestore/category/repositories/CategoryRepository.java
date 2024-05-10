package com.project.mobilestore.category.repositories;

import com.project.mobilestore.category.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndNameNot(String name, String excludedName);

    Page<Category> findAllByNameContaining(String name, Pageable pageable);

    Integer countByNameContaining(String name);
}
