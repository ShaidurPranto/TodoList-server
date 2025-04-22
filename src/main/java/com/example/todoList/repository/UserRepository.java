package com.example.todoList.repository;

import com.example.todoList.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    // Custom query methods can be defined here if needed
    User findByEmail(String email);
}
