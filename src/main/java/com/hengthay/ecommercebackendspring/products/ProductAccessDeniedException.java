package com.hengthay.ecommercebackendspring.products;

public class ProductAccessDeniedException extends RuntimeException {
    public ProductAccessDeniedException(String message) {
        super(message);
    }
}
