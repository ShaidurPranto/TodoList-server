#!/bin/bash

export BACKEND_URL=http://localhost:8080
export FRONTEND_URL=http://localhost:5173

SPRING_DATA_MONGODB_URI="mongodb+srv://pranto:admin@cluster0.lw1c8hg.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0" \
SPRING_DATA_MONGODB_DATABASE=todoList \
GOOGLE_CLIENT_ID=329814224341-gdcuhkd75fja16d28dp14sj8vif43lvg.apps.googleusercontent.com \
GOOGLE_CLIENT_SECRET=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 \
mvn spring-boot:run
