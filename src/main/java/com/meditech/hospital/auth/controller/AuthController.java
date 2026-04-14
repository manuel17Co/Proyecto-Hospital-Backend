package com.meditech.hospital.auth.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meditech.hospital.auth.dto.LoginRequestDto;
import com.meditech.hospital.auth.dto.LoginResponseDto;
import com.meditech.hospital.auth.dto.RefreshRequestDto;
import com.meditech.hospital.auth.dto.ResetPasswordCodeRequestDto;
import com.meditech.hospital.auth.dto.ResetPasswordCodeResponseDto;
import com.meditech.hospital.auth.dto.ResetPasswordRequestDto;
import com.meditech.hospital.auth.dto.ResetPasswordResponseDto;
import com.meditech.hospital.auth.dto.SignupRequestDto;
import com.meditech.hospital.auth.dto.SignupResponseDto;
import com.meditech.hospital.auth.dto.ValidatePasswordOtpRequest;
import com.meditech.hospital.auth.dto.ValidatePasswordOtpResponse;
import com.meditech.hospital.auth.dto.VerificationEmailRequestDto;
import com.meditech.hospital.auth.dto.VerificationEmailResponseDto;
import com.meditech.hospital.auth.dto.VerifyEmailOtpRequest;
import com.meditech.hospital.auth.dto.VerifyEmailOtpResponse;
import com.meditech.hospital.auth.service.AuthService;
import com.meditech.hospital.common.exception.BadRequestException;
import com.meditech.hospital.common.exception.UnauthorizedException;
import com.meditech.hospital.users.dto.GetUserDto;
import com.meditech.hospital.users.dto.UpdateUserDto;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto loginRequest) {
        return this.authService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @PostMapping("/signup")
    public SignupResponseDto signup(@Valid @RequestBody SignupRequestDto signupRequest) {
        System.out.println("Signup response generated");
        SignupResponseDto response = this.authService.signup(signupRequest);
        if (response == null) {
            throw new BadRequestException("Error during signup");
        }
        return response;
    }

    @PostMapping("/refresh")
    public LoginResponseDto refresh(@Valid @RequestBody RefreshRequestDto request) {
        return this.authService.refreshToken(request.getRefreshToken());
    }

        @PostMapping("/forgot-password")
    public ResetPasswordCodeResponseDto forgotPassword(@Valid @RequestBody ResetPasswordCodeRequestDto request) {
        return this.authService.resetPasswordCode(request.getEmail());
    }

    @PostMapping("/validate-reset-code")
    public ValidatePasswordOtpResponse validateResetCode(@Valid @RequestBody ValidatePasswordOtpRequest request) {
        return this.authService.validateOtp(request.getEmail(), request.getOtp());
    }

    @PostMapping("/reset-password")
    public ResetPasswordResponseDto resetPassword(@Valid @RequestBody ResetPasswordRequestDto request) {
        return this.authService.resetPassword(request.getToken(), request.getNewPassword());
    }

    @PostMapping("/verify-email")
    public VerificationEmailResponseDto verifyEmail(@Valid @RequestBody VerificationEmailRequestDto request) {
        return this.authService.verifyAccount(request.getEmail());
    }

    @PostMapping("/validate-email-otp")
    public VerifyEmailOtpResponse validateEmailOtp(@Valid @RequestBody VerifyEmailOtpRequest request) {
        System.out.println("Entrando al primer endpoint para validar correo");
        return this.authService.validateEmailOtp(request.getEmail(), request.getOtp());
    }

    @GetMapping("/me")
    public GetUserDto me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Unauthorized user");
        }
        return this.authService.me(authentication.getName());
    }

    @PatchMapping("/me")
    public GetUserDto updateMe(Authentication authentication, @Valid @RequestBody UpdateUserDto request) {
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("____________________Auth:"+authentication);
            throw new UnauthorizedException("Unauthorized user");
        }

        return this.authService.updateMe(authentication.getName(), request);
    }
}
