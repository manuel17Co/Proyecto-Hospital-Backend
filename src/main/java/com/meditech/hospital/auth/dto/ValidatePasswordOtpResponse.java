package com.meditech.hospital.auth.dto;

public class ValidatePasswordOtpResponse {
    private String token;

    public ValidatePasswordOtpResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
