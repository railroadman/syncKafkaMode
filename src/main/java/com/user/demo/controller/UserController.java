package com.user.demo.controller;

import com.user.demo.entites.User;
import com.user.demo.mappers.UserMapper;
import com.user.demo.model.dto.AddUserDto;
import com.user.demo.model.dto.UpdateUserDto;
import com.user.demo.model.dto.UserResponseDto;
import com.user.demo.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Operation(summary = "Get all users")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @Operation(summary = "Get user by ID usinng Rest")
    @GetMapping("/rest/{id}")
    public User getRestUserById(@PathVariable Long id) {
        return userService.getRestUserById(id);
    }

    @Operation(summary = "Get user by ID usinng Rest")
    @GetMapping("/kafka/{id}")
    public UserResponseDto getKafkaUserById(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        var result = userService.getAddressUsingKafka(id);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("Время выполнения запроса: " + duration + " мс");
        return  result;
    }

    @Operation(summary = "Create a new user")
    @PostMapping
    public User createUser(@RequestBody AddUserDto addUserDto) {
        User user = userMapper.addUserDtoToUser(addUserDto);
        return userService.createUser(user, addUserDto.getAddressesId());
    }

    @Operation(summary = "Update user by ID")
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody UpdateUserDto updateUserDto) {
        User user = userMapper.updateUserDtoToUser(updateUserDto);
        return userService.updateUser(id, user, updateUserDto.getAddressesId());
    }

    @Operation(summary = "Delete user by ID")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}