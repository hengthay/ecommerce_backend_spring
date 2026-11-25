package com.hengthay.ecommercebackendspring.orders;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrderDto {
    private UUID id;
    private String status;
    private LocalDateTime createdAt;
    private List<OrderItemDto> items;
    private BigDecimal totalAmount;
    private ShippingAddressDto addressDto;
}
