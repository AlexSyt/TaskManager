package com.example.alex.taskmanager.db;

import com.example.alex.taskmanager.Task;

import java.util.ArrayList;
import java.util.List;

public class UnitOfWork {
    private final TaskDao taskDao;
    private final List<Task> create;
    private final List<Task> update;
    private final List<Task> delete;

    public UnitOfWork(TaskDao taskDao) {
        this.taskDao = taskDao;
        create = new ArrayList<>();
        update = new ArrayList<>();
        delete = new ArrayList<>();
    }

    public void create(Task task) {
        create.add(task);
    }

    public void update(Task task) {
        update.add(task);
    }

    public void delete(Task task) {
        if (create.contains(task)) create.remove(task);
        else delete.add(task);
    }

    public void commit() {
        for (Task task : delete) taskDao.delete(task);
        for (Task task : create) taskDao.create(task);
        for (Task task : update) taskDao.update(task);
        create.clear();
        update.clear();
        delete.clear();
    }
}
