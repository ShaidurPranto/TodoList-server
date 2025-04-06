package com.example.todoList.controller;

import com.example.todoList.model.User;
import com.example.todoList.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // api for demonstration purpose (no usage)
    @GetMapping("/pranto")
    public String getPranto() {
        return "Hello Pranto!";
    }

    // Endpoint to create a new user
    @PostMapping("/signup")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // Endpoint to login a user
    @PostMapping("/login")
    public String loginUser(@RequestBody User user) {
        return userService.loginUser(user);
    }
}
