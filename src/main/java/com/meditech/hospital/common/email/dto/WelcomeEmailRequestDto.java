package com.meditech.hospital.common.email.dto;

import java.util.Map;

public class WelcomeEmailRequestDto {
    private String appName;
    private String username;
    private String year;

    public WelcomeEmailRequestDto(String appName, String username, String year) {
        this.appName = appName;
        this.username = username;
        this.year = year;
    }

    public Map<String, Object> getAll() {
        return Map.of(
            "app_name", appName,
            "username", username,
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
