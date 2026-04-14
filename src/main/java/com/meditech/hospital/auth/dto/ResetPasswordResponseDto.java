package com.meditech.hospital.auth.dto;

public class ResetPasswordResponseDto {
    private String message;

    public ResetPasswordResponseDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
