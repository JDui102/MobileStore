package com.project.mobilestore.service;

import com.project.mobilestore.category.dtos.CategoryDTO;
import com.project.mobilestore.category.entities.Category;
import com.project.mobilestore.category.repositories.CategoryRepository;
import com.project.mobilestore.category.services.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private CategoryDTO testCategoryDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testCategoryDTO = CategoryDTO.builder()
                .id(1L)
                .name("Test Category")
                .createOn(LocalDate.now())
                .updateOn(LocalDate.now())
                .build();
    }

    @Test
    public void testSaveCategory_Success() {
        when(categoryRepository.existsByName("Test Category")).thenReturn(false);
        when(categoryRepository.save(any())).thenReturn(categoryService.mapToCategory(testCategoryDTO));

        Category savedCategory = categoryService.saveCategory(testCategoryDTO);

        assertNotNull(savedCategory);
        assertEquals("Test Category", savedCategory.getName());
    }

    @Test
    public void testGetCategory_Success() {
        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(categoryService.mapToCategory(testCategoryDTO)));

        Optional<CategoryDTO> retrievedCategory = categoryService.getCategory(1L);

        assertEquals("Test Category", retrievedCategory.get().getName());
    }

    @Test
    public void testGetCategory_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<CategoryDTO> categoryDTO = categoryService.getCategory(1L);

        assertFalse(categoryDTO.isPresent());
    }

    @Test
    public void testGetCategories_Success() {
        Page<Category> testPage = new PageImpl<>
                (Collections.singletonList(categoryService.mapToCategory(testCategoryDTO)));
        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(testPage);
        when(categoryRepository.count()).thenReturn(1L);

        int validPageNumber = 1;

        Page<CategoryDTO> categories = categoryService.getCategories(validPageNumber);

        assertEquals(1, categories.getTotalElements());
        assertEquals("Test Category", categories.getContent().get(0).getName());
    }

    @Test
    public void testDeleteCategoryById_Success() {
        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(categoryService.mapToCategory(testCategoryDTO)));

        assertTrue(categoryService.deleteCategoryById(1L));
    }

    @Test
    public void testSearchByCategoryName() {
        Page<Category> testPage = new PageImpl<>
                (Collections.singletonList(categoryService.mapToCategory(testCategoryDTO)));
        when(categoryRepository.findAllByNameContaining(anyString(), any(Pageable.class))).thenReturn(testPage);

        Page<CategoryDTO> categories = categoryService.searchByCategoryName("Test", 1);

        assertEquals(1, categories.getTotalElements());
        assertEquals("Test Category", categories.getContent().get(0).getName());
    }
}
