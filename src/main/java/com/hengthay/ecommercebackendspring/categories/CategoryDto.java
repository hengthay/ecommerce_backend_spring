package com.hengthay.ecommercebackendspring.categories;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
    private String type;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}
