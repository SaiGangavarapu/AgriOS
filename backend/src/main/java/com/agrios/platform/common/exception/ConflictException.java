package com.agrios.platform.common.exception;

public final class ConflictException extends BusinessException {
    public ConflictException(String code, String message) {
        super(code, message, 409);
    }
}
