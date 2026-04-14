package com.meditech.hospital.auth.dto;

public class VerifyEmailOtpResponse {
    private String token;

    public VerifyEmailOtpResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
