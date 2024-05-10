package com.project.mobilestore.category.services;

import com.project.mobilestore.category.dtos.CategoryDTO;
import com.project.mobilestore.category.entities.Category;
import com.project.mobilestore.category.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final MessageSource messageSource;
    private final ModelMapper mapper;

    // Convert to CategoryDTO
    public CategoryDTO mapToCategoryDto(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .createOn(category.getCreateOn())
                .updateOn(category.getUpdateOn())
                .build();
    }

    // Convert to CategoryDTO
    public Category mapToCategory(CategoryDTO categoryDTO) {
        return Category.builder()
                .id(categoryDTO.getId())
                .name(categoryDTO.getName())
                .createOn(categoryDTO.getCreateOn())
                .updateOn(categoryDTO.getUpdateOn())
                .build();
    }

    // Get pages of paginated categories
    public Pageable getPaginatedCategories(Integer pageNumber, String categoryName) {
        int DEFAULT_PAGE_SIZE = 10;
        long count;
        if (categoryName != null && !categoryName.isEmpty()) {
            count = categoryRepository.countByNameContaining(categoryName);
        } else {
            count = categoryRepository.count();
        }

        int totalPages = (int) Math.ceil((double) count / DEFAULT_PAGE_SIZE);

        if (pageNumber < 1 || (count == 0 && pageNumber > 1) || (count != 0 && pageNumber > totalPages)) {
            throw new IllegalArgumentException(messageSource.getMessage("invalid-page-number",
                    null, LocaleContextHolder.getLocale()));
        }

        return PageRequest.of(
                pageNumber - 1,
                DEFAULT_PAGE_SIZE,
                Sort.by(Sort.Direction.DESC, "id"));
    }

    // Get a category by id
    @Override
    public Optional<CategoryDTO> getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).map(this::mapToCategoryDto);
    }

    // Get categories
    @Override
    public Page<CategoryDTO> getCategories(Integer pageNumber) {
        // Return categories
        return categoryRepository.findAll(getPaginatedCategories(pageNumber, null)).map(this::mapToCategoryDto);
    }

    // Check exist by name
    @Override
    public boolean existByName(String name) {
        return categoryRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameAndNameNot(String name, String excludeName) {
        return categoryRepository.existsByNameAndNameNot(name, excludeName);
    }

    // Save the category and return it
    @Override
    public Category saveCategory(CategoryDTO categoryDTO) {
        // Save category
        return categoryRepository.save(mapToCategory(categoryDTO));
    }

    @Override
    public boolean deleteCategoryById(Long deleteCategoryId) {
        categoryRepository.deleteById(deleteCategoryId);
        return true;
    }

    @Override
    public Page<CategoryDTO> searchByCategoryName(String categoryName, Integer pageNumber) {
        // Return categories
        return categoryRepository.findAllByNameContaining(categoryName,getPaginatedCategories(pageNumber, categoryName)).map(this::mapToCategoryDto);
    }
}
