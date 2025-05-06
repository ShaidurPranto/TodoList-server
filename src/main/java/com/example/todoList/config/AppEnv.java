package com.example.todoList.config;

import io.github.cdimascio.dotenv.Dotenv;

public class AppEnv {
    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    public static String getMongoUri() {
        return dotenv.get("MONGODB_URI");
    }

    public static String getMongoDatabase() {
        return dotenv.get("MONGODB_DATABASE");
    }

    public static String getFrontendUrl() {
        return dotenv.get("FRONTEND_URL");
    }
}
