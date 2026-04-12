package com.meditech.hospital.auth.service;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.meditech.hospital.auth.dto.LoginResponseDto;
import com.meditech.hospital.auth.dto.SignupRequestDto;
import com.meditech.hospital.auth.dto.SignupResponseDto;
import com.meditech.hospital.auth.enums.TokenType;
import com.meditech.hospital.common.exception.BadRequestException;
import com.meditech.hospital.common.exception.ForbiddenException;
import com.meditech.hospital.common.exception.UnauthorizedException;
import com.meditech.hospital.users.dto.CreateUserDto;
import com.meditech.hospital.users.dto.GetUserDto;
import com.meditech.hospital.users.dto.UpdateUserDto;
import com.meditech.hospital.users.entity.User;
import com.meditech.hospital.users.service.UserService;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final StringRedisTemplate redis;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtService jwtService, StringRedisTemplate redis) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.redis = redis;
    }

    private User validateCredentials(String email, String password) {
        User user = userService.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public LoginResponseDto login(String email, String password) throws UnauthorizedException {
        User user = this.validateCredentials(email, password);
        if (user == null) {
            throw new UnauthorizedException("Invalid email or password");
        }
        if (!Boolean.TRUE.equals(user.getVerified())) {
            throw new ForbiddenException("User account is not verified. Please verify your email before logging in");
        }
        String accessToken = jwtService.generateToken(user.getEmail(), TokenType.ACCESS);
        String refreshToken = jwtService.generateToken(user.getEmail(), TokenType.REFRESH);
        return new LoginResponseDto(accessToken, refreshToken);
    }

    public SignupResponseDto signup(SignupRequestDto signupRequest) {

        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setName(signupRequest.getName());
        createUserDto.setSurname(signupRequest.getSurname());
        createUserDto.setEmail(signupRequest.getEmail());
        createUserDto.setPassword(signupRequest.getPassword());

        User newUser = userService.create(createUserDto);

        if (newUser == null) {
            throw new BadRequestException("User with email " + signupRequest.getEmail() + " already exists");
        }

        String accessToken = jwtService.generateToken(newUser.getEmail(), TokenType.ACCESS);
        String refreshToken = jwtService.generateToken(newUser.getEmail(), TokenType.REFRESH);
        return new SignupResponseDto(accessToken, refreshToken);
    }

    public LoginResponseDto refreshToken(String refreshToken) throws UnauthorizedException {
        if (jwtService.isTokenExpired(refreshToken)) {
            throw new UnauthorizedException("Invalid refresh token");
        }
        String email = jwtService.extractEmail(refreshToken);
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new UnauthorizedException("User not found");
        }
        String newAccessToken = jwtService.generateToken(user.getEmail(), TokenType.ACCESS);
        String newRefreshToken = jwtService.generateToken(user.getEmail(), TokenType.REFRESH);

        return new LoginResponseDto(newAccessToken, newRefreshToken);
    }

    public GetUserDto me(String email) {
        if (email == null || email.isBlank()) {
            throw new UnauthorizedException("Unauthorized user");
        }

        User user = userService.findByEmail(email);
        if (user == null) {
            throw new UnauthorizedException("User not found");
        }

        return new GetUserDto(
            user.getId(),
            user.getName(),
            user.getSurname(),
            user.getEmail(),
            user.getVerified()
        );
    }

    public GetUserDto updateMe(String email, UpdateUserDto updateUserDto) {
        if (email == null || email.isBlank()) {
            throw new UnauthorizedException("Unauthorized user");
        }

        User user = userService.findByEmail(email);
        if (user == null) {
            throw new UnauthorizedException("User not found");
        }

        updateUserDto.setVerified(null);
        User updatedUser = userService.update(user.getId(), updateUserDto);

        if (updatedUser == null) {
            throw new BadRequestException("Error updating user");
        }

        return new GetUserDto(
            updatedUser.getId(),
            updatedUser.getName(),
            updatedUser.getSurname(),
            updatedUser.getEmail(),
            updatedUser.getVerified()
        );
    }

}
