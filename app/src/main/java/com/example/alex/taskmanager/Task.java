package com.example.alex.taskmanager;

import java.util.UUID;

public class Task {
    private UUID id;
    private String text;
    private String createdDate;

    public Task(UUID id, String text, String createdDate) {
        this.id = id;
        this.text = text;
        this.createdDate = createdDate;
    }

    public Task(String text, String createdDate) {
        id = UUID.randomUUID();
        this.text = text;
        this.createdDate = createdDate;
    }

    public UUID getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
