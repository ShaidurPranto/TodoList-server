package com.example.todoList;

import com.example.todoList.util.AppEnv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class TodoListApplication {

	public static void main(String[] args) {

		SpringApplication.run(TodoListApplication.class, args);

		System.out.println("todoListApplication started");
		System.out.println("Testing environment variables");
		System.out.println("FRONTEND_URL: " + AppEnv.getFrontendUrl());
		System.out.println("BACKEND_URL: " + AppEnv.getBackendUrl());
	}

	// api for demonstration purpose (no usage)
	@RequestMapping("/")
	public String hello() {
		return "Hello, ToDo!";
	}
}

