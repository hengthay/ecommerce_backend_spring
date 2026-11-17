package com.hengthay.ecommercebackendspring.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private Jwt accessToken;
    private Jwt refreshToken;
}
