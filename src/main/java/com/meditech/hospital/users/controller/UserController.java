package com.meditech.hospital.users.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meditech.hospital.common.exception.BadRequestException;
import com.meditech.hospital.users.dto.CreateUserDto;
import com.meditech.hospital.users.dto.GetUserDto;
import com.meditech.hospital.users.entity.User;
import com.meditech.hospital.users.service.UserService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public GetUserDto createUser(@Valid @RequestBody CreateUserDto user) {
        User createdUser = userService.create(user);

        if (createdUser == null) {
            throw new BadRequestException("Error creating user");
        }

        GetUserDto createdUserDto = new GetUserDto(createdUser.getId(), createdUser.getName(), createdUser.getSurname(), createdUser.getEmail(), createdUser.getVerified());

        return createdUserDto;
    }

    @GetMapping("")
    public List<GetUserDto> getUsers() {
        List<User> users = userService.findAll();
        List<GetUserDto> userDtos = users.stream()
                                    .map(user -> new GetUserDto(user.getId(), user.getName(), user.getSurname(), user.getEmail(), user.getVerified()))
                                    .toList();

        System.out.println(userDtos);
        return userDtos;
    }
}