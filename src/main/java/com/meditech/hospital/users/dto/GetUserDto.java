package com.meditech.hospital.users.dto;

public class GetUserDto {
    private String id;

    private String name;

    private String surname;

    private String email;

    private Boolean verified;

    public GetUserDto() {}

    public GetUserDto(String id, String name, String surname, String email, Boolean verified) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.verified = verified;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
}