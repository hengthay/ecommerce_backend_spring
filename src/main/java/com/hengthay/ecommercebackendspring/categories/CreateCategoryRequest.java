package com.hengthay.ecommercebackendspring.categories;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCategoryRequest {
    @NotBlank(message = "Category name is required.")
    @Size(max = 255, message = "Category name should be less than 255 characters.")
    private String name;

    private String description;

    @NotBlank(message = "Category type is required.")
    @Size(max = 50, message = "Category type should be less than 50 characters.")
    private String type;
}
