package com.meditech.hospital.auth.dto;

public class VerifyEmailOtpResponse {
    private String message;

    public VerifyEmailOtpResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
