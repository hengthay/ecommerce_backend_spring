package com.hengthay.ecommercebackendspring.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid Email Format.")
    @Size(max = 255, message = "Email should be less than 255 characters")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 255, message = "Password should between 8 to 255 characters.")
    private String password;
}
