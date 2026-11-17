package com.hengthay.ecommercebackendspring.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChangePasswordRequest {
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 255, message = "Password must be 8 to 255 characters")
    private String oldPassword;
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 255, message = "Password must be 8 to 255 characters")
    private String newPassword;
}
