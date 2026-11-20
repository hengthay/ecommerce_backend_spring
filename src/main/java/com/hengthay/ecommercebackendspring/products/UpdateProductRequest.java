package com.hengthay.ecommercebackendspring.products;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class UpdateProductRequest {
    @NotBlank(message = "Title is required.")
    @Size(max = 255, message = "Title should be less than 255 characters.")
    private String title;

    @NotNull(message = "Price is required.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0.")
    private BigDecimal price;

    @NotNull(message = "Stock is required.")
    @Min(value = 0, message = "Stock should be greater than or equal to 0.")
    private Integer stock;

    private String description;

    @NotNull(message = "Category ID is required.")
    private Long categoryId;
}
