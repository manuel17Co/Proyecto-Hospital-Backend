package com.meditech.hospital.auth.service;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.meditech.hospital.auth.dto.LoginResponseDto;
import com.meditech.hospital.auth.dto.ResetPasswordCodeResponseDto;
import com.meditech.hospital.auth.dto.SignupRequestDto;
import com.meditech.hospital.auth.dto.SignupResponseDto;
import com.meditech.hospital.auth.enums.TokenType;
import com.meditech.hospital.common.exception.BadRequestException;
import com.meditech.hospital.common.exception.ForbiddenException;
import com.meditech.hospital.common.exception.UnauthorizedException;
import com.meditech.hospital.common.helpers.OtpGenerator;
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

    public ResetPasswordCodeResponseDto resetPasswordCode(String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new UnauthorizedException("User not found");
        }
        String otp = OtpGenerator.generateOtp();
        redis.opsForValue().set(
            "otp:reset:" + email,
            passwordEncoder.encode(otp),
            Duration.ofMinutes(10)
        );

        EmailSender emailSender = EmailSender.getInstance();

        ChangePasswordEmailRequestDto variables = new ChangePasswordEmailRequestDto("CanchaFacil", user.getName(), otp, "10", "2026");

        emailSender.sendChangePasswordEmail(email, variables);

        return new ResetPasswordCodeResponseDto("OTP sent to email, valid for 10 minutes");
    }

    public ValidatePasswordOtpResponse validateOtp(String email, String otp) throws UnauthorizedException {
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new UnauthorizedException("User not found");
        }
        String storedOtp = redis.opsForValue().get("otp:reset:" + email);
        if (storedOtp == null || !passwordEncoder.matches(otp, storedOtp)) {
            throw new UnauthorizedException("Invalid or expired OTP");
        }
        redis.delete("otp:reset:" + email);
        String token = jwtService.generateToken(user.getEmail(), TokenType.PASSWORD_RESET);
        redis.opsForValue().set("password-reset:" + token, user.getEmail(), Duration.ofMinutes(TokenExpiration.get(TokenType.PASSWORD_RESET) / 60));
        return new ValidatePasswordOtpResponse(token);
    }

    public ResetPasswordResponseDto resetPassword(String token, String newPassword) {
        TokenType tokenType = jwtService.extractTokenType(token);
        if (tokenType != TokenType.PASSWORD_RESET) {
            throw new UnauthorizedException("Invalid token type");
        }
        String email = jwtService.extractEmail(token);
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new UnauthorizedException("User not found");
        }
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setPassword(newPassword);
        User updatedUser = userService.update(user.getId(), updateUserDto);

        return new ResetPasswordResponseDto("User " + updatedUser.getEmail() + " password updated successfully");
    }

    public VerificationEmailResponseDto verifyAccount(String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new UnauthorizedException("User not found");
        }

        if (user.getVerified()) {
            throw new BadRequestException("User already verified");
        }

        String otp = OtpGenerator.generateOtp();
        redis.opsForValue().set(
            "otp:validate:" + email,
            passwordEncoder.encode(otp),
            Duration.ofMinutes(10)
        );
        EmailSender emailSender = EmailSender.getInstance();

        VerificationEmailRequestDto variables = new VerificationEmailRequestDto("CanchaFacil", user.getName(), otp, "10", "2026");

        emailSender.sendVerificationEmail(email, variables);

        return new VerificationEmailResponseDto("OTP sent to email, valid for 10 minutes");
    }

    public VerifyEmailOtpResponse validateEmailOtp(String email, String otp) throws UnauthorizedException {
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new UnauthorizedException("User not found");
        }
        String storedOtp = redis.opsForValue().get("otp:validate:" + email);
        if (storedOtp == null || !passwordEncoder.matches(otp, storedOtp)) {
            throw new UnauthorizedException("Invalid or expired OTP");
        }
        redis.delete("otp:validate:" + email);
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setVerified(true);
        User updatedUser = userService.update(user.getId(), updateUserDto);
        return new VerifyEmailOtpResponse("User " + updatedUser.getEmail() + " verified successfully");
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
