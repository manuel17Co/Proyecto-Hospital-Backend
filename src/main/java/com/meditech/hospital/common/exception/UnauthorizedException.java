package com.meditech.hospital.common.exception;

public class UnauthorizedException extends RuntimeException {
    private String status;
    public UnauthorizedException(String message) {
        super(message);
        this.status = "Unauthorized";
    }

    public String getStatus() {
        return status;
    }
}
