package com.hengthay.ecommercebackendspring.carts;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartProductDto {
    private Long id;
    private String title;
    private BigDecimal price;
    private String imageUrl;
}
