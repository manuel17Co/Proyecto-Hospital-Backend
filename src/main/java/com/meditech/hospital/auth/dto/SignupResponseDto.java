package com.meditech.hospital.auth.dto;

public class SignupResponseDto {
    private String accessToken;
    private String refreshToken;

    public SignupResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
