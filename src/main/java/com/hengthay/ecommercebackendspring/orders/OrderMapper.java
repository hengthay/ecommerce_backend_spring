package com.hengthay.ecommercebackendspring.orders;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);

    @Mapping(target = "orderId", source = "order.id")
    OrderItemDto toDto(OrderItem orderItem);
}
