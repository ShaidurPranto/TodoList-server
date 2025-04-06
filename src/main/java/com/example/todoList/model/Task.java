package com.example.todoList.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class Task {
    private String definition;
    private String status;
    private Date eventTime;

    public Task() {
    }
    public Task(String definition, String status, Date eventTime) {
        this.definition = definition;
        this.status = status;
        this.eventTime = eventTime;
    }
    public String getDefinition() {
        return definition;
    }
    public void setDefinition(String definition) {
        this.definition = definition;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Date getEventTime() {
        return eventTime;
    }
    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }
    @Override
    public String toString() {
        return "Task{" +
                "definition='" + definition + '\'' +
                ", status='" + status + '\'' +
                ", eventTime=" + eventTime +
                '}';
    }
}
