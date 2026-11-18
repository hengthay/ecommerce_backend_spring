package com.hengthay.ecommercebackendspring.auth;

import com.hengthay.ecommercebackendspring.config.ErrorDto;
import com.hengthay.ecommercebackendspring.users.UserDto;
import com.hengthay.ecommercebackendspring.users.UserMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtConfig jwtConfig;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public JwtReponse login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        var loginResult = authService.login(request);

        var cookie = new Cookie("refreshToken", loginResult.getRefreshToken().toString());
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setPath("/api/auth");

        // add cookie to response
        response.addCookie(cookie);

        return new JwtReponse(loginResult.getAccessToken().toString());
    };

    @PostMapping("/refresh")
    public JwtReponse refresh(
            @CookieValue(value = "refreshToken", required = false) String refreshToken
    ) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BadCredentialsException("Refresh token is missing");
        }

        var accessToken = authService.refreshAccessToken(refreshToken);

        return new JwtReponse(accessToken.toString());
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        var user = authService.getCurrentUser();

        if(user == null)
            return ResponseEntity.notFound().build();

        var userDto = userMapper.toDto(user);

        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/health-check")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.status(HttpStatus.OK).body(
                Map.of("status", "ok")
        );
    }
    @ExceptionHandler(BadCredentialsException.class)
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
    // Handle unauthorize if invalid information
//    @ExceptionHandler(BadCredentialsException.class)
//    public ResponseEntity<Void> handleBadCredentialsException() {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//    }
}
