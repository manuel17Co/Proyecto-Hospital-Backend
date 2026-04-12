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
import com.meditech.hospital.auth.dto.SignupRequestDto;
import com.meditech.hospital.auth.dto.SignupResponseDto;
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
        SignupResponseDto response = this.authService.signup(signupRequest);
        if (response == null) {
            throw new BadRequestException("Error during signup");
        }
        return response;
    }

    @PostMapping("/refresh")
    public LoginResponseDto refresh(@Valid @RequestBody RefreshRequestDto request) {
        System.out.println("Refresh token request received: " + request.getRefreshToken());
        return this.authService.refreshToken(request.getRefreshToken());
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
            throw new UnauthorizedException("Unauthorized user");
        }

        return this.authService.updateMe(authentication.getName(), request);
    }
}
