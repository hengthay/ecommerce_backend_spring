package com.hengthay.ecommercebackendspring.auth;

import com.hengthay.ecommercebackendspring.config.ErrorDto;
import com.hengthay.ecommercebackendspring.users.UserAccessDeniedException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {
        authService.login(request);

        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse("Login was successfully."));
    };

    @ExceptionHandler(UserAccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleBadCredentialsException(Exception exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ErrorDto(
                        404,
                        exception.getMessage(),
                        null,
                        Instant.now()
                )
        );
    }
}
