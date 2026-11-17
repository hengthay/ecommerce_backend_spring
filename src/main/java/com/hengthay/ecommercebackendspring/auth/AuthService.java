package com.hengthay.ecommercebackendspring.auth;

import com.hengthay.ecommercebackendspring.users.UserAccessDeniedException;
import com.hengthay.ecommercebackendspring.users.UserNotFoundException;
import com.hengthay.ecommercebackendspring.users.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;


    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        return new LoginResponse("Login successful.");
    }
}
