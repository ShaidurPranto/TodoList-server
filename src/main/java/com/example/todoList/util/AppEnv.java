package com.example.todoList.util;

import io.github.cdimascio.dotenv.Dotenv;

public class AppEnv {
    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    public static String getFrontendUrl() {
        return dotenv.get("FRONTEND_URL");
    }

    public static String getBackendUrl() {
        return dotenv.get("BACKEND_URL");
    }
}
