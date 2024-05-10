package com.project.mobilestore.category.services;

import com.project.mobilestore.category.dtos.CategoryDTO;
import com.project.mobilestore.category.entities.Category;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface CategoryService {
    // Get category
    Optional<CategoryDTO> getCategory(Long id);

    // Get categories
    Page<CategoryDTO> getCategories(Integer pageNumber);

    // Check exist by name
    boolean existByName(String name);

    // Check exists by name but exclude ...
    boolean existsByNameAndNameNot(String name, String excludeName);

    // Save Category
    Category saveCategory(CategoryDTO category);

    // Delete Category
    boolean deleteCategoryById(Long id);

    // Search by category name
    Page<CategoryDTO> searchByCategoryName(String categoryName, Integer pageNumber);
}

