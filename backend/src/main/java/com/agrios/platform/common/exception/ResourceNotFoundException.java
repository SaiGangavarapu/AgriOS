package com.agrios.platform.common.exception;

public final class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String code, String message) {
        super(code, message, 404);
    }
}
