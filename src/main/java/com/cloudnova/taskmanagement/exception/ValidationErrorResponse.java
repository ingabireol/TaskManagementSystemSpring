package com.cloudnova.taskmanagement.exception;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Error response for validation failures
 * Extends ErrorResponse to include field-specific errors
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class ValidationErrorResponse {
    private String message;
    private int status;
    private String error;
    private LocalDateTime timestamp;
    private Map<String, String> fieldErrors;
}