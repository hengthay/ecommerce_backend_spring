package com.hengthay.ecommercebackendspring.addresses;

import com.hengthay.ecommercebackendspring.users.UserDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddressDto {
    private Long id;
    private UserDto userDto;
    private String street;
    private String city;
    private String country;
    private String postalCode;
    private String phoneNumber;
    private boolean active;
    private LocalDateTime createdAt;
}
