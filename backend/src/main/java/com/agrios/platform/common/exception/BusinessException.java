package com.agrios.platform.common.exception;

public class BusinessException extends RuntimeException {
    private final String code;
    private final int status;
    private final boolean retryable;

    public BusinessException(String code, String message, int status) {
        this(code, message, status, false);
    }

    public BusinessException(String code, String message, int status, boolean retryable) {
        super(message);
        this.code = code;
        this.status = status;
        this.retryable = retryable;
    }

    public String code() { return code; }
    public int status() { return status; }
    public boolean retryable() { return retryable; }
}
