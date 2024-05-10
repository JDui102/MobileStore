package com.project.mobilestore.category.controllers;

import com.project.mobilestore.category.services.CategoryService;
import com.project.mobilestore.category.dtos.CategoryDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.project.mobilestore.config.security.SecurityUtil;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final MessageSource messageSource;
    private final SecurityUtil securityUtil;

    // Get message form message.properties
    private String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    // Create information for CU form
    public void createInfoCUForm(Model model, CategoryDTO categoryDTO, Long id)
    {
        String title = getMessage("category.create-form-title");
        String url = "/categories/new";
        String buttonSubmit = getMessage("button.submit.create.name");

        if (id != null) {
            title = getMessage("category.edit-form-title");
            url = "/categories/"+ id +"/edit";
            buttonSubmit = getMessage("button.submit.edit.name");
        }

        model.addAttribute("title", title);
        model.addAttribute("url", url);
        model.addAttribute("buttonSubmit", buttonSubmit);
        model.addAttribute("category", categoryDTO);
    }

    // Get the list of categories
    @GetMapping("/categories")
    public String getCategories(@RequestParam(defaultValue = "1") Integer page, Model model) {
        try {
            // Get user information
            model.addAttribute("user", securityUtil.getUserInfo());

            // Get the list of categories from the category service
            Page<CategoryDTO> categoriesDTO = categoryService.getCategories(page);

            // Add the list of categories and check if it's empty to the model
            model.addAttribute("categories", categoriesDTO);
            model.addAttribute("isEmptyCategories", categoriesDTO.getContent().isEmpty());
        } catch (Exception ex) {
            // Handle the exception if there's an error and redirect to the page-not-found error page
            model.addAttribute("failed", ex.getMessage());
            return "page-not-found";
        }

        // Return the page to display the list of categories
        return "category/categories";
    }

    // Show the page for creating a new category
    @GetMapping("/categories/new")
    public String showSaveCategoryPage(Model model) {
        createInfoCUForm(model, CategoryDTO.builder().build(), null);
        return "category/cu-category";
    }

    // Process the creation of a new category
    @PostMapping("/categories/new")
    public String saveCategory(@Valid @ModelAttribute("category") CategoryDTO categoryDTO,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes)
    {
        // Check Validation
        if (categoryService.existByName(categoryDTO.getName()))
            result.rejectValue("name", "category.exist");
        if (result.hasErrors()) {
            createInfoCUForm(model, categoryDTO, null);
            return "category/cu-category";
        }

        // Save the new category
        categoryService.saveCategory(categoryDTO);

        // Add a success flash attribute with a message retrieved from message.properties
        redirectAttributes.addFlashAttribute("success", getMessage("category.create-category-success"));

        // Redirect to the categories page after successful creation
        return "redirect:/categories";
    }

    // Show the page for updating a category
    @GetMapping("/categories/{id}/edit")
    public String showEditCategoryPage(@PathVariable("id") @NotNull Long id, Model model) {
        Optional<CategoryDTO> categoryDTO = categoryService.getCategory(id);
        // Check already exist
        if (!categoryDTO.isPresent()) {
            model.addAttribute("failed", getMessage("category.not-exist"));
            return "page-not-found";
        }

        createInfoCUForm(model, categoryDTO.get(), id);
        return "category/cu-category";
    }

    // Update the information of a category
    @PostMapping("/categories/{id}/edit")
    public String updateCategory(@PathVariable Long id,
                                 @Valid @ModelAttribute("category") CategoryDTO categoryDTO,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes)
    {
        // Check already exist
        Optional<CategoryDTO> existCategoryOptionalDTO = categoryService.getCategory(id);
        if (!existCategoryOptionalDTO.isPresent())
            return "redirect:/categories/"+ id +"/edit";

        CategoryDTO existCategoryDTO = existCategoryOptionalDTO.get();

        // Check Validation
        if (categoryService.existsByNameAndNameNot(categoryDTO.getName(), existCategoryDTO.getName()))
            result.rejectValue("name", "category.exist");
        if (result.hasErrors()) {
            createInfoCUForm(model, categoryDTO, id);
            return "category/cu-category";
        }

        // Set data
        existCategoryDTO.setName(categoryDTO.getName());
        existCategoryDTO.setUpdateOn(LocalDate.now());

        // Save the updated category
        categoryService.saveCategory(existCategoryDTO);

        // Add a success flash attribute with a message retrieved from message.properties
        redirectAttributes.addFlashAttribute("success", getMessage("category.update-category-success"));

        // Redirect to the categories page after successful update
        return "redirect:/categories";
    }

    // Delete a category
    @GetMapping("/categories/{id}/delete")
    public String deleteCategory(@PathVariable @NotNull Long id, RedirectAttributes redirectAttributes) {
        if (!categoryService.getCategory(id).isPresent())
            redirectAttributes.addFlashAttribute("failed", getMessage("category.not-exist"));
        else {
            // Delete category
            categoryService.deleteCategoryById(id);

            // Add a success flash attribute with a message retrieved from message.properties
            redirectAttributes.addFlashAttribute("success", getMessage("category.delete-category-success"));
        }

        // Redirect to the categories page
        return "redirect:/categories";
    }

    // Search for categories by name
    @GetMapping("/search-category")
    public String searchCategoryByName(@RequestParam @NotNull String name,
                                       @RequestParam(defaultValue = "1") Integer page,
                                       Model model)
    {
        try {
            // Get user information
            model.addAttribute("user", securityUtil.getUserInfo());

            // Search for categories by name
            Page<CategoryDTO> categories = categoryService.searchByCategoryName(name, page);

            // Add the search name and the list of categories to the model
            model.addAttribute("searchName", name);
            model.addAttribute("categories", categories);
        } catch (Exception ex) {
            // Handle the exception if there's an error and redirect to the page-not-found error page
            model.addAttribute("failed", ex.getMessage());
            return "page-not-found";
        }

        // Return the page to display the list of categories
        return "category/categories";
    }
}
