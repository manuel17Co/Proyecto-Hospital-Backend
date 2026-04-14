package com.meditech.hospital.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UpdateUserDto {
    @Size(min = 2, max = 50)
    private String name = null;

    @Size(min = 2, max = 50)
    private String surname = null;

    @Email
    @Size(min = 5, max = 255)
    private String email = null;

    @Size(min = 8, max = 20)
    private String password = null;

    private Boolean verified = null;

    public UpdateUserDto() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
}