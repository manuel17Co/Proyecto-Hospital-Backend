package com.meditech.hospital.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class ResetPasswordCodeRequestDto {
    @NotBlank
    @NotEmpty
    private String email;

    public ResetPasswordCodeRequestDto(String email) {
        this.email = email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
