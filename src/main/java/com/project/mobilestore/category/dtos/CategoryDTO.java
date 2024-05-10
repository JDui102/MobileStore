package com.project.mobilestore.category.dtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Data
@Builder
public class CategoryDTO {
    private Long id;

    @Column(unique = true)
    @NotEmpty(message = "Category name can't be left empty!")
    @Size(max = 50, message = "Category name must be less than or equal to 50 characters!")
    private String name;

    @CreationTimestamp
    private LocalDate createOn;

    @UpdateTimestamp
    private  LocalDate updateOn;
}
