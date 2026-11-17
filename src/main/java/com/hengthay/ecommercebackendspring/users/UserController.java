package com.hengthay.ecommercebackendspring.users;

import com.hengthay.ecommercebackendspring.config.ErrorDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/active")
    public ResponseEntity<List<UserDto>> getOnlyActiveUsers() {
        return ResponseEntity.ok(userService.getOnlyActiveUsers());
    }

    @RequestMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            @Valid @RequestBody RegisterUserRequest request)
    {
        return ResponseEntity.status(201).body(userService.registerUser(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request)
    {
        return ResponseEntity.status(200).body(userService.updateUser(id, request));
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<UserDto> updateUserRole(
            @PathVariable Long id,
            @Valid @RequestBody AdminUpdateUserRoleRequest request)
    {
        return ResponseEntity.status(200).body(userService.updateUserRole(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id)
    {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<UserDto> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest request)
    {
        userService.changePassword(id, request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/activate-user")
    public ResponseEntity<String> activateUser(@PathVariable Long id) {
        userService.activateUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("User activate successful.");
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFound(
            UserNotFoundException exception
    ) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorDto(
                        404,
                        exception.getMessage(),
                        null,
                        Instant.now()
                )
        );
    }

    @ExceptionHandler(UserAccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleAccessDenied(
            UserAccessDeniedException exception
    ) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorDto(
                        404,
                        exception.getMessage(),
                        null,
                        Instant.now()
                )
        );
    }
}
