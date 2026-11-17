package com.hengthay.ecommercebackendspring.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateUserRequest {
    @Email(message = "Email must be valid @hotmall.com")
    @Size(max = 255, message = "Email must be less than 255 characters")
    private String email;

    @NotBlank(message = "Username is required")
    @Size(min = 8, max = 255, message = "Username must be 8 to 255 characters")
    private String username;
}
