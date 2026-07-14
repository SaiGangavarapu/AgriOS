package com.agrios.platform.common.web;

import com.agrios.platform.common.api.ApiError;
import com.agrios.platform.common.exception.BusinessException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    ResponseEntity<ApiError> handleBusiness(BusinessException ex) {
        return ResponseEntity.status(ex.status()).body(error(
                ex.status(), ex.code(), ex.getMessage(), ex.retryable(), List.of()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        List<ApiError.FieldError> fields = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toFieldError)
                .toList();
        return ResponseEntity.badRequest().body(error(
                400, "REQUEST_VALIDATION_FAILED", "The request contains invalid values.", false, fields));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiError> handleUnexpected(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error(
                500, "INTERNAL_PROCESSING_ERROR", "An unexpected error occurred.", false, List.of()));
    }

    private ApiError.FieldError toFieldError(FieldError error) {
        return new ApiError.FieldError(error.getField(), "INVALID_VALUE", error.getDefaultMessage());
    }

    private ApiError error(int status, String code, String message, boolean retryable,
                           List<ApiError.FieldError> fields) {
        String value = MDC.get(CorrelationIdFilter.MDC_KEY);
        UUID correlationId;
        try { correlationId = value == null ? UUID.randomUUID() : UUID.fromString(value); }
        catch (IllegalArgumentException ignored) { correlationId = UUID.randomUUID(); }

        return new ApiError(Instant.now(), status, code, message,
                "error." + code.toLowerCase(), correlationId, retryable, fields, Map.of());
    }
}
