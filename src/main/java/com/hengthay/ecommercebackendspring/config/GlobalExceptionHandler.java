package com.hengthay.ecommercebackendspring.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleUnreadableErrors(
    ) {
        return ResponseEntity.badRequest().body(
                new ErrorDto(
                        400,
                        "Invalid request body",
                        null,
                        Instant.now()
                )
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationErrors(
            MethodArgumentNotValidException exception
    ) {
        // Storing value is key-value pairs
        var errors = new HashMap<String, String>();

        // Iterate through exception and add it to errors object
        exception.getBindingResult().getFieldErrors().forEach(
                error -> {
                    String message = error.isBindingFailure()
                            ? "Invalid format for " + error.getField()
                            : error.getDefaultMessage();
                    errors.put(error.getField(), message);
                }
        );

        // Send back the error messages
        return ResponseEntity.badRequest().body(
                new ErrorDto(
                        400,
                        "Validation failed",
                        errors,
                        Instant.now()
                )
        );
    }

    // General unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGeneralErrors(Exception exception) {

        exception.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorDto(
                                500,
                                "Something went wrong. Please try again later.",
                                null,
                                Instant.now()
                        )
                );
    }
}
