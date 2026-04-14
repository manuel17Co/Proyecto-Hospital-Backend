package com.meditech.hospital.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class RefreshRequestDto {
    @NotBlank
    private String refreshToken;

    public RefreshRequestDto() {
    }

    public RefreshRequestDto(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

