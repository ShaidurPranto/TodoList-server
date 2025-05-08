package com.example.todoList.service;

import com.example.todoList.authJWT.JWTService;
import com.example.todoList.model.Task;
import com.example.todoList.model.User;
import com.example.todoList.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    private final UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(7);


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    private List<Task> sortTaskByEventTimeDescending(List<Task> tasks) {
        return tasks
                .stream()
                .sorted((task1, task2) -> task2.getEventTime().compareTo(task1.getEventTime()))
                .collect(Collectors.toList());
    }

    // create a new user
    public User createUser(User user) {
        System.out.println("inside the user service , create user");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            System.out.println("User with this email already exists");
            return null; // or throw an exception
        } else {
            user.setTasks(new ArrayList<>());
            userRepository.save(user);
            System.out.println("User created successfully");
            return user;
        }
    }

    // get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // log in a user
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(user.getEmail());

            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            // cookie.setSecure(true); // must be set for same-site = None to work
            cookie.setPath("/");
            cookie.setMaxAge(60*30); // 30 minute
            cookie.setAttribute("SameSite","None"); // Set SameSite attribute to None
            response.addCookie(cookie);

            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
        }
    }


    // get all tasks of a user
    public ResponseEntity<?> getUserTasks(HttpServletRequest request) {
        String email = jwtService.getEmailFromRequest(request);
        if (email != null) {
            User user = userRepository.findByEmail(email);
            if (user != null) {
                List<Task> tasks = user.getTasks();
                if (tasks != null && !tasks.isEmpty()) {
                    // Filter tasks with eventTime after the current time
                    List<Task> futureTasks = tasks.stream()
                            .filter(task -> task.getEventTime().after(new Date()))
                            .collect(Collectors.toList());

                    return ResponseEntity.ok(futureTasks);
                } else {
                    return ResponseEntity.ok(Collections.emptyList()); // No tasks
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }


    // add a task to a user
    public String addTask(Task task, HttpServletRequest request) {
        String email = jwtService.getEmailFromRequest(request);
        if (email != null) {
            User user = userRepository.findByEmail(email);
            if (user != null) {
                List<Task> tasks = user.getTasks();
                if (tasks == null) {
                    tasks = new ArrayList<>();
                } else {
                    for (Task t : tasks) {
                        if (t.getDefinition().equals(task.getDefinition())
                                && t.getEventTime().equals(task.getEventTime())) {
                            return "Task already exists";
                        }
                    }
                }

                tasks.add(task);
                user.setTasks(tasks);
                userRepository.save(user);
                return "Task added successfully";
            } else {
                return "User not found";
            }
        } else {
            return "Invalid token";
        }
    }


    // update a task of a user
    public ResponseEntity<?> updateTask(Task task, HttpServletRequest request) {
        String email = jwtService.getEmailFromRequest(request);
        if (email != null) {
            User user = userRepository.findByEmail(email);
            if (user != null) {
                List<Task> tasks = user.getTasks();
                if (tasks != null && !tasks.isEmpty()) {
                    boolean taskUpdated = false;
                    for (Task t : tasks) {
                        if (t.getDefinition().equals(task.getDefinition())
                                && t.getEventTime().equals(task.getEventTime())) {
                            t.setStatus(task.getStatus());
                            taskUpdated = true;
                            break;
                        }
                    }

                    if (!taskUpdated) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
                    }

                    user.setTasks(tasks);
                    userRepository.save(user);
                    System.out.println("Task updated successfully");

                    // Filter tasks to only include future tasks
                    Date now = new Date();
                    List<Task> futureTasks = tasks.stream()
                            .filter(t -> t.getEventTime().after(now))
                            .collect(Collectors.toList());

                    return ResponseEntity.ok(futureTasks);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No tasks found for this user");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    // delete a task of a user
    public ResponseEntity<?> deleteTask(Task task, HttpServletRequest request) {
        String email = jwtService.getEmailFromRequest(request);
        if (email != null) {
            User user = userRepository.findByEmail(email);
            if (user != null) {
                List<Task> tasks = user.getTasks();
                if (tasks != null && !tasks.isEmpty()) {
                    // Create a copy of the tasks list to avoid modifying it during iteration
                    List<Task> tasksToRemove = new ArrayList<>(tasks);

                    for (Task t : tasksToRemove) {
                        if (t.getDefinition().equals(task.getDefinition())
                                && t.getEventTime().equals(task.getEventTime())) {

                            // Remove the task after looping through the list
                            tasks.remove(t);

                            user.setTasks(tasks);
                            userRepository.save(user);

                            // Filter tasks to only include future tasks
                            Date now = new Date();
                            List<Task> futureTasks = tasks.stream()
                                    .filter(tt -> tt.getEventTime().after(now))
                                    .collect(Collectors.toList());

                            return ResponseEntity.ok(futureTasks);
                        }
                    }

                    // If the task wasn't found, return a not found response
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No tasks found for this user");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    // history : get all the previous completed tasks of a user
    public ResponseEntity<?> getPreviousCompletedTasks(HttpServletRequest request) {
        String email = jwtService.getEmailFromRequest(request);
        if (email != null) {
            User user = userRepository.findByEmail(email);
            if (user != null) {
                List<Task> tasks = user.getTasks();
                if (tasks != null && !tasks.isEmpty()) {

                    List<Task> previousCompletedTasks = tasks;

                    // Filter tasks with status "done"
                    previousCompletedTasks = previousCompletedTasks.stream()
                            .filter(task -> task.getStatus().equals("done"))
                            .collect(Collectors.toList());

                    // Sort tasks by eventTime in descending order
                    previousCompletedTasks = sortTaskByEventTimeDescending(previousCompletedTasks);

                    return ResponseEntity.ok(previousCompletedTasks);
                } else {
                    return ResponseEntity.ok(Collections.emptyList()); // No tasks
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    // history : get all the previous uncompleted tasks of a user
    public ResponseEntity<?> getPreviousIncompletedTasks(HttpServletRequest request) {
    String email = jwtService.getEmailFromRequest(request);
        if (email != null) {
            User user = userRepository.findByEmail(email);
            if (user != null) {
                List<Task> tasks = user.getTasks();
                if (tasks != null && !tasks.isEmpty()) {

                    // Filter tasks with eventTime before the current time
                    List<Task> previousUncompletedTasks = tasks.stream()
                            .filter(task -> task.getEventTime().before(new Date()))
                            .collect(Collectors.toList());

                    // Filter tasks with status "pending"
                    previousUncompletedTasks = previousUncompletedTasks.stream()
                            .filter(task -> task.getStatus().equals("pending"))
                            .collect(Collectors.toList());

                    // Sort tasks by eventTime in descending order
                    previousUncompletedTasks = sortTaskByEventTimeDescending(previousUncompletedTasks);

                    return ResponseEntity.ok(previousUncompletedTasks);
                } else {
                    return ResponseEntity.ok(Collections.emptyList()); // No tasks
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    // history : get all the tasks of a user
    public ResponseEntity<?> getAllTasks(HttpServletRequest request) {
        String email = jwtService.getEmailFromRequest(request);
        if (email != null) {
            User user = userRepository.findByEmail(email);
            if (user != null) {
                List<Task> tasks = user.getTasks();
                if (tasks != null && !tasks.isEmpty()) {

                    // Sort tasks by eventTime in descending order
                    tasks = sortTaskByEventTimeDescending(tasks);

                    return ResponseEntity.ok(tasks);
                } else {
                    return ResponseEntity.ok(Collections.emptyList()); // No tasks
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    public ResponseEntity<?> getUserName(HttpServletRequest request) {
        String email = jwtService.getEmailFromRequest(request);
        if (email != null) {
            User user = userRepository.findByEmail(email);
            if (user != null) {
                return ResponseEntity.ok(user.getName());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
