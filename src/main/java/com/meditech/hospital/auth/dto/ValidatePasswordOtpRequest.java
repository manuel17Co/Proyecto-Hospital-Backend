package com.meditech.hospital.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class ValidatePasswordOtpRequest {
    @NotBlank
    @NotEmpty
    private String email;

    @NotBlank
    @NotEmpty
    private String otp;

    public ValidatePasswordOtpRequest(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
