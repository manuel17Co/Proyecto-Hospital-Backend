package com.meditech.hospital.common.exception;

public class BadRequestException extends RuntimeException {
    private String status;
    public BadRequestException(String message) {
        super(message);
        this.status = "Bad Request";
    }

    public String getStatus() {
        return status;
    }
}
