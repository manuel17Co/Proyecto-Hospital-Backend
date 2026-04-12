package com.meditech.hospital.common.exception;

public class NotFoundException extends RuntimeException {
    private String status;

    public NotFoundException(String message) {
        super(message);
        this.status = "Not Found";
    }

    public String getStatus() {
        return status;
    }
}
