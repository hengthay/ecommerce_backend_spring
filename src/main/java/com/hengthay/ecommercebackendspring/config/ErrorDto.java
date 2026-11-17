package com.hengthay.ecommercebackendspring.config;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ErrorDto {
    private int status;
    private String message;
    private Object errors;
    private Instant timestamp;
}
