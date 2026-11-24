package com.hengthay.ecommercebackendspring.checkout;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CheckoutRequest {
    @NotNull(message = "Cart ID is required.")
    private UUID cartId;

    @NotNull(message = "Address is required.")
    private Long addressId;
}
