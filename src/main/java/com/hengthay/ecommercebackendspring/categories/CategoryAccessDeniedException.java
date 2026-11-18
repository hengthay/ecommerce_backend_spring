package com.hengthay.ecommercebackendspring.categories;

public class CategoryAccessDeniedException extends RuntimeException {
    public CategoryAccessDeniedException(String message) {
        super(message);
    }
}
