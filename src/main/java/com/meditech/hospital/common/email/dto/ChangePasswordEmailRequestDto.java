package com.meditech.hospital.common.email.dto;

import java.util.Map;

public class ChangePasswordEmailRequestDto {
    private String appName;
    private String username;
    private String code;
    private String expirationMinutes;
    private String year;

    public ChangePasswordEmailRequestDto(String appName, String username, String code, String expirationMinutes, String year) {
        this.appName = appName;
        this.username = username;
        this.code = code;
        this.expirationMinutes = expirationMinutes;
        this.year = year;
    }

    public Map<String, Object> getAll() {
        return Map.of(
            "app_name", appName,
            "username", username,
            "code", code,
            "expiration_minutes", expirationMinutes,
            "year", year
        );
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExpirationMinutes() {
        return expirationMinutes;
    }

    public void setExpirationMinutes(String expirationMinutes) {
        this.expirationMinutes = expirationMinutes;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
