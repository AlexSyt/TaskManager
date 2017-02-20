package com.example.alex.taskmanager;

public class Task {
    private long id;
    private String text;
    private String createdDate;

    public Task(long id, String text, String createdDate) {
        this.id = id;
        this.text = text;
        this.createdDate = createdDate;
    }

    public long getId() {
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
