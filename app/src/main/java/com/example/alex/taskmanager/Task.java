package com.example.alex.taskmanager;

public class Task {
    private long id;
    private String text;

    public Task(String text) {
        this.text = text;
    }

    public Task(long id, String text) {
        this.id = id;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
