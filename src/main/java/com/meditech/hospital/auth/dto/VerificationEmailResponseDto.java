package com.meditech.hospital.auth.dto;

public class VerificationEmailResponseDto {
    private String message;

    public VerificationEmailResponseDto(String message) {
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
