package com.hengthay.ecommercebackendspring.orders;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderItemDto {
    private UUID id;
    private UUID orderId;
    private OrderProductDto product;
}
