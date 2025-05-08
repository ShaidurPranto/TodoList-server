#!/bin/bash

export BACKEND_URL=http://localhost:8080
export FRONTEND_URL=http://localhost:5173

SPRING_DATA_MONGODB_URI="mongodb+srv://pranto:admin@cluster0.lw1c8hg.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0" \
SPRING_DATA_MONGODB_DATABASE=todoList \
mvn spring-boot:run
