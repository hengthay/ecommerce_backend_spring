package com.hengthay.ecommercebackendspring.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@AllArgsConstructor
@Getter
public class UserDto {
    private Long id;
    private String email;
    private String username;
    private Role role;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}
