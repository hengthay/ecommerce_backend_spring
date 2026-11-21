package com.hengthay.ecommercebackendspring.carts;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CartMapper {

    // map to correct field
    @Mapping(target = "userId", source = "user.id")
    // Copy items from Cart to the items property of CartDto
    @Mapping(target = "items", source = "items")
    // For the totalPrice field in CartDto, execute this Java expression: cart.getTotalPrice().
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    // Use to mapping from Entity to Dto object
    CartDto toDto(Cart cart);

    Cart toEntity(CartDto cartDto);

    // map to correct field
    @Mapping(target = "cartId", source = "cart.id")
    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);

    void update(CartDto cartDto, @MappingTarget Cart cart);
}
