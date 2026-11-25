package com.hengthay.ecommercebackendspring.addresses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAddressRequest {
    @NotBlank(message = "Street is required.")
    @Size(max = 100, message = "Street must be less than or equal to 100 characters.")
    private String street;

    @NotBlank(message = "City is required.")
    @Size(max = 255, message = "City must be less than or equal to 255 characters.")
    private String city;

    @NotBlank(message = "Country is required.")
    @Size(max = 255, message = "Country must be less than or equal to 255 characters.")
    private String country;

    @NotBlank(message = "Postal code is required.")
    @Size(max = 20, message = "Postal Code must be less than or equal to 20 characters.")
    private String postalCode;

    @NotBlank(message = "Phone number is required.")
    @Size(max = 50, message = "Phone number must be less than or equal to 50 characters.")
    private String phoneNumber;
}
