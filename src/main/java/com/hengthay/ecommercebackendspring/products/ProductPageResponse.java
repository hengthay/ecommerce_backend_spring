package com.hengthay.ecommercebackendspring.products;

import org.springframework.data.domain.Page;

import java.util.List;

public record ProductPageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
    public static <T> ProductPageResponse<T> from(Page<T> pageData) {
        return new ProductPageResponse<>(
                pageData.getContent(),
                pageData.getNumber(),
                pageData.getSize(),
                pageData.getTotalElements(),
                pageData.getTotalPages(),
                pageData.isFirst(),
                pageData.isLast()
        );
    }
}
