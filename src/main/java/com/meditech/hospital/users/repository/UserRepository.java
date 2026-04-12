package com.meditech.hospital.users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meditech.hospital.users.entity.User;


public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}