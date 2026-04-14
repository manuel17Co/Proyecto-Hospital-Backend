package com.meditech.hospital.auth.dto;

public class ResetPasswordCodeResponseDto {
    private String message;

    public ResetPasswordCodeResponseDto(String message) {
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
