package com.example.todoList.controller;

import com.example.todoList.model.Task;
import com.example.todoList.model.User;
import com.example.todoList.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
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

    // api for testing purpose (no usage)
    @GetMapping("/test")
    public String test() {
        System.out.println("Test endpoint hit");
        return "hello";
    }

    // Endpoint to create a new user
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        System.out.println("Received user signup request: ");
        User createdUser = userService.createUser(user);
        if (createdUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }
        return ResponseEntity.ok("User created successfully");
    }

    // Endpoint to log in a user
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user , HttpServletResponse response) {
        System.out.println("User login request: ");
        System.out.println(user);
        return userService.loginUser(user,response);
    }

    // Endpoint to get tasks of a user ( future tasks only )
    @GetMapping("/tasks")
    public ResponseEntity<?> getUserTasks(HttpServletRequest request) {
        return userService.getUserTasks(request);
    }

    // Endpoint to add a task to a user
    @PostMapping("/tasks/add")
    public String addTask(@RequestBody Task task, HttpServletRequest request) {
        return userService.addTask(task, request);
    }

    // Endpoint to delete a task of a user ( returns future tasks of user )
    @DeleteMapping("/tasks/delete")
    public ResponseEntity<?> deleteTask(@RequestBody Task task, HttpServletRequest request) {
        return userService.deleteTask(task, request);
    }

    // Endpoint to update a task of a user
    @PutMapping("/tasks/update")
    public ResponseEntity<?> updateTask(@RequestBody Task task, HttpServletRequest request) {
        return userService.updateTask(task, request);
    }

    // Endpoint to get all the previous completed tasks of a user
    @GetMapping("/tasks/previousCompleted")
    public ResponseEntity<?> getPreviousCompletedTasks(HttpServletRequest request) {
        return userService.getPreviousCompletedTasks(request);
    }

    // Endpoint to get all the previous uncompleted tasks of a user
    @GetMapping("/tasks/previousIncompleted")
    public ResponseEntity<?> getPreviousIncompletedTasks(HttpServletRequest request) {
        return userService.getPreviousIncompletedTasks(request);
    }

    // Endpoint to get all the tasks of a user
    @GetMapping("/tasks/all")
    public ResponseEntity<?> getAllTasks(HttpServletRequest request) {
        return userService.getAllTasks(request);
    }
}
