package com.meditech.hospital.common.exception;

public class ForbiddenException extends RuntimeException {
    private String status;

    public ForbiddenException(String message) {
        super(message);
        this.status = "Forbidden";
    }

    public String getStatus() {
        return status;
    }
}
