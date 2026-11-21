package com.hengthay.ecommercebackendspring.carts;

public class CartEmptyException extends RuntimeException {
    public CartEmptyException(String message) {
        super(message);
    }
}
