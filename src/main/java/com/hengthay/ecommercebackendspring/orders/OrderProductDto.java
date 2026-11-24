package com.hengthay.ecommercebackendspring.orders;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderProductDto {
    private Long id;
    private String title;
    private BigDecimal price;
    private String imageUrl;
}
