package com.meditech.hospital.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class CreateUserDto {

    @NotBlank
    @NotEmpty
    @Size(min = 2, max = 50)
    private String name;

    @NotBlank
    @NotEmpty
    @Size(min = 2, max = 50)
    private String surname;

    @NotBlank
    @NotEmpty
    @Size(min = 5, max = 255)
    private String email;

    @NotBlank
    @NotEmpty
    @Size(min = 8, max = 20)
    private String password;

    public CreateUserDto() {}

    public CreateUserDto(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

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
}