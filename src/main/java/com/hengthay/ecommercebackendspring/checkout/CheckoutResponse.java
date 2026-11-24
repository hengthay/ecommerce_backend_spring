package com.hengthay.ecommercebackendspring.checkout;

import lombok.Data;

import java.util.UUID;

@Data
public class CheckoutResponse {
    private UUID orderId;
    private String checkoutUrl;

    public CheckoutResponse(UUID orderId, String checkoutUrl) {
        this.orderId = orderId;
        this.checkoutUrl = checkoutUrl;
    }
}
