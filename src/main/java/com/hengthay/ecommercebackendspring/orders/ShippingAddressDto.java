package com.hengthay.ecommercebackendspring.orders;

import lombok.Data;

@Data
public class ShippingAddressDto {
    private String street;
    private String city;
    private String country;
    private String postalCode;
    private String phoneNumber;
}
