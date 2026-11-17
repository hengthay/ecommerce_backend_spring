package com.hengthay.ecommercebackendspring.users;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AdminUpdateUserRoleRequest {
    @NotNull(message = "Role is required")
    private Role role;
}
