package com.hengthay.ecommercebackendspring.products;

import com.hengthay.ecommercebackendspring.categories.Category;
import com.hengthay.ecommercebackendspring.categories.CategorySummaryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String title;
    private BigDecimal price;
    private Integer stock;
    private String imageUrl;
    private String description;
    private boolean active;
    private CategorySummaryDto categoryDto;
    private Instant createdAt;
    private Instant updatedAt;
}
