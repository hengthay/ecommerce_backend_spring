package com.hengthay.ecommercebackendspring.orders;

import com.hengthay.ecommercebackendspring.addresses.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "addressDto", source = "address")
    OrderDto toDto(Order order);

    ShippingAddressDto toDto(Address address);

    @Mapping(target = "orderId", source = "order.id")
    OrderItemDto toDto(OrderItem orderItem);
}
