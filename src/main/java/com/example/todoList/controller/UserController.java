package com.example.todoList.controller;

import com.example.todoList.model.Task;
import com.example.todoList.model.User;
import com.example.todoList.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
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
        System.out.println("User login request: ");
        System.out.println(user);
        return userService.loginUser(user);
    }

    // Endpoint to get tasks of a user
    @GetMapping("/tasks")
    public ResponseEntity<?> getUserTasks(HttpServletRequest request) {
        return userService.getUserTasks(request);
    }

    // Endpoint to add a task to a user
    @PostMapping("/tasks/add")
    public String addTask(@RequestBody Task task, HttpServletRequest request) {
        return userService.addTask(task, request);
    }

    // Endpoint to delete a task of a user
    @DeleteMapping("/tasks/delete")
    public ResponseEntity<?> deleteTask(@RequestBody Task task, HttpServletRequest request) {
        return userService.deleteTask(task, request);
    }

    // Endpoint to update a task of a user
    @PutMapping("/tasks/update")
    public ResponseEntity<?> updateTask(@RequestBody Task task, HttpServletRequest request) {
        return userService.updateTask(task, request);
    }
}
