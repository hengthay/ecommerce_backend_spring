package com.hengthay.ecommercebackendspring.categories;

public class CategoryNameAlreadyExistException extends RuntimeException {
    public CategoryNameAlreadyExistException(String message) {
        super(message);
    }
}
