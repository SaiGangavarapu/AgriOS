package com.agrios.platform.common.api;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ApiError(
        Instant timestamp,
        int status,
        String code,
        String message,
        String messageKey,
        UUID correlationId,
        boolean retryable,
        List<FieldError> fieldErrors,
        Map<String, Object> details) {

    public record FieldError(String field, String code, String message) {}
}
