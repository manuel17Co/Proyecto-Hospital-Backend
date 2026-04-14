package com.meditech.hospital.users.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.meditech.hospital.users.dto.CreateUserDto;
import com.meditech.hospital.users.dto.UpdateUserDto;
import com.meditech.hospital.users.entity.User;
import com.meditech.hospital.users.repository.UserRepository;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(CreateUserDto user) {
        User existingUser = this.findByEmail(user.getEmail());
        if (existingUser != null) {
            return null;
        }

        User newUser = new User();

        String password = user.getPassword();

        newUser.setName(user.getName());
        newUser.setSurname(user.getSurname());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setVerified(false);

        User created = userRepository.save(newUser);

        return created;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User update(String id, UpdateUserDto updateUserDto) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }

        if (updateUserDto.getName() != null) {
            user.setName(updateUserDto.getName());
        }
        if (updateUserDto.getSurname() != null) {
            user.setSurname(updateUserDto.getSurname());
        }
        if (updateUserDto.getEmail() != null) {
            user.setEmail(updateUserDto.getEmail());
        }
        if (updateUserDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateUserDto.getPassword()));
        }
        if (updateUserDto.getVerified() != null) {
            user.setVerified(updateUserDto.getVerified());
        }

        return userRepository.save(user);
    }
}