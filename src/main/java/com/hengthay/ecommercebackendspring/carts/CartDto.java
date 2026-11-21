package com.hengthay.ecommercebackendspring.carts;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Data
public class CartDto {
    private UUID id;
    private Long userId;
    private boolean active;
    private Set<CartItemDto> items = new LinkedHashSet<>();
    private BigDecimal totalPrice = BigDecimal.ZERO;
}
