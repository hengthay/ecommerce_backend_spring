package com.hengthay.ecommercebackendspring.carts;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CartItemDto {
    private UUID id;
    private UUID cartId;
    private CartProductDto product;
    private Integer quantity;
    private BigDecimal totalPrice;
}
