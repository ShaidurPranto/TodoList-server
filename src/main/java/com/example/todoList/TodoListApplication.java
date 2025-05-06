package com.example.todoList;

import com.example.todoList.config.AppEnv;
import com.example.todoList.config.DotenvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class TodoListApplication {

	public static void main(String[] args) {

		// Load environment variables from .env file to system properties
		//DotenvLoader.loadEnv();

		SpringApplication.run(TodoListApplication.class, args);
		System.out.println("todoListApplication started");
	}

	// api for demonstration purpose (no usage)
	@RequestMapping("/")
	public String hello() {
		return "Hello, ToDo!";
	}
}

