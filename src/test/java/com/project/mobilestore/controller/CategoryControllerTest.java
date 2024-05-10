package com.project.mobilestore.controller;

import com.project.mobilestore.category.controllers.CategoryController;
import com.project.mobilestore.category.dtos.CategoryDTO;
import com.project.mobilestore.category.entities.Category;
import com.project.mobilestore.category.services.CategoryServiceImpl;
import com.project.mobilestore.config.security.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CategoryControllerTest {
    @Mock
    private CategoryServiceImpl categoryService;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private CategoryController categoryController;

    CategoryDTO testCategoryDTO;
    RedirectAttributes redirectAttributes;
    BindingResult bindingResult;
    Model model;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testCategoryDTO = CategoryDTO.builder()
                .id(1L)
                .name("Test Category")
                .createOn(LocalDate.now())
                .updateOn(LocalDate.now())
                .build();

        redirectAttributes = mock(RedirectAttributes.class);
        bindingResult = mock(BindingResult.class);
        model = mock(Model.class);
    }

    @Test
    void testShowSaveCategoryPage() {
        String result = categoryController.showSaveCategoryPage(model);
        assertEquals("category/cu-category", result);
    }

    @Test
    void testSaveCategory_Success() {
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Message");
        String result = categoryController.saveCategory(testCategoryDTO, bindingResult, model, redirectAttributes);

        assertEquals("redirect:/categories", result);
        verify(categoryService, times(1)).saveCategory(any());
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("success"), anyString());
    }

    @Test
    void testSaveCategoryWhenCategoryExists() {
        when(categoryService.existByName(testCategoryDTO.getName())).thenReturn(true);
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = categoryController.saveCategory(testCategoryDTO, bindingResult, model, redirectAttributes);


        verify(bindingResult).rejectValue("name", "category.exist");
        verify(model).addAttribute(eq("category"), any(CategoryDTO.class));
        assertEquals("category/cu-category", viewName);
    }

    @Test
    void testSaveCategoryWithValidationError() {
        CategoryDTO categoryDTO = CategoryDTO.builder().name("").build();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = categoryController.saveCategory(categoryDTO, bindingResult, model, redirectAttributes);

        verify(model).addAttribute(eq("category"), any(CategoryDTO.class));
        assertEquals("category/cu-category", viewName);
    }

    @Test
    void testGetCategories_NotEmpty() {
        Page<CategoryDTO> testPage = new PageImpl<>
                (Collections.singletonList(testCategoryDTO));

        when(categoryService.getCategories(anyInt())).thenReturn(testPage);

        String result = categoryController.getCategories(1, model);

        assertEquals("category/categories", result);
        verify(categoryService, times(1)).getCategories(1);
        verify(model, times(1)).addAttribute("categories", testPage);
        verify(model, times(1)).addAttribute("isEmptyCategories", false);
    }

    @Test
    void testGetCategories_Empty() {
        Page<CategoryDTO> testPage = new PageImpl<>(new ArrayList<>());

        when(categoryService.getCategories(anyInt())).thenReturn(testPage);

        String result = categoryController.getCategories(1, model);

        assertEquals("category/categories", result);
        verify(categoryService, times(1)).getCategories(1);
        verify(model, times(1)).addAttribute("categories", testPage);
        verify(model, times(1)).addAttribute("isEmptyCategories", true);
    }

    @Test
    void testShowEditCategoryPage_Success() {
        when(categoryService.getCategory(anyLong())).thenReturn(Optional.of(testCategoryDTO));

        String result = categoryController.showEditCategoryPage(1L, model);

        assertEquals("category/cu-category", result);
        verify(model, times(1)).addAttribute(eq("category"), any(CategoryDTO.class));
    }

    @Test
    void testShowUpdateCategoryPage_Failure() {
        when(categoryService.getCategory(anyLong())).thenReturn(Optional.empty());

        String result = categoryController.showEditCategoryPage(1L, model);

        assertEquals("page-not-found", result);
    }

    @Test
    void testUpdateCategory_Success() {
        when(categoryService.getCategory(eq(testCategoryDTO.getId()))).thenReturn(Optional.of(testCategoryDTO));
        when(categoryService.saveCategory(any(CategoryDTO.class))).thenReturn(any(Category.class));

        testCategoryDTO.setName("Update Name");
        String result = categoryController.updateCategory(testCategoryDTO.getId(), testCategoryDTO, bindingResult, model, redirectAttributes);

        assertEquals("redirect:/categories", result);
        verify(categoryService, times(1)).saveCategory(any(CategoryDTO.class));
    }

    @Test
    void testUpdateCategory_Failure_Exist_Name() {
        when(categoryService.getCategory(anyLong())).thenReturn(Optional.of(testCategoryDTO));
        when(categoryService.existByName(testCategoryDTO.getName())).thenReturn(true);
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = categoryController.updateCategory(testCategoryDTO.getId(), testCategoryDTO, bindingResult, model, redirectAttributes);

        assertEquals("category/cu-category", result);
    }

    @Test
    void testUpdateCategory_Failure_Not_Found() {
        when(categoryService.getCategory(eq(testCategoryDTO.getId()))).thenReturn(Optional.empty());

        String result = categoryController.updateCategory(testCategoryDTO.getId(), testCategoryDTO, bindingResult, model, redirectAttributes);

        assertEquals("redirect:/categories/1/edit", result);
    }


    @Test
    void testDeleteCategory_Success() {
        when(categoryService.getCategory(eq(testCategoryDTO.getId()))).thenReturn(Optional.of(testCategoryDTO));

        String result = categoryController.deleteCategory(1L, redirectAttributes);

        verify(categoryService, times(1)).deleteCategoryById(1L);
        assertEquals("redirect:/categories", result);
    }

    @Test
    void testDeleteCategory_Failure() {
        when(categoryService.getCategory(testCategoryDTO.getId())).thenReturn(Optional.empty());

        String result = categoryController.deleteCategory(testCategoryDTO.getId(), redirectAttributes);

        verify(categoryService, never()).deleteCategoryById(testCategoryDTO.getId());
        assertEquals("redirect:/categories", result);
    }

    @Test
    void testSearchCategoryByName() {
        Page<CategoryDTO> testPage = new PageImpl<>
                (Collections.singletonList(testCategoryDTO));

        when(categoryService.searchByCategoryName(anyString(), anyInt())).thenReturn(testPage);

        String result = categoryController.searchCategoryByName("Test", 1, model);

        assertEquals("category/categories", result);
        verify(categoryService, times(1)).searchByCategoryName("Test", 1);
        verify(model, times(1)).addAttribute("searchName", "Test");
        verify(model, times(1)).addAttribute("categories", testPage);
    }
}

