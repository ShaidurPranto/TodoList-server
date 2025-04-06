package com.example.todoList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@CrossOrigin
public class TodoListApplication {

	public static void main(String[] args) {

		SpringApplication.run(TodoListApplication.class, args);
		System.out.println("Hello java");
	}

	// api for demonstration purpose (no usage)
	@RequestMapping("/")
	public String hello() {
		return "Hello, ToDo!";
	}
}

