package com.example.alex.taskmanager.db;

import com.example.alex.taskmanager.Task;

import java.util.ArrayList;
import java.util.List;

public class UnitOfWork {
    private final TaskDao taskDao;
    private final List<Task> registeredForCreate;
    private final List<Task> registeredForUpdate;
    private final List<Task> registeredForDelete;

    public UnitOfWork(TaskDao taskDao) {
        this.taskDao = taskDao;
        registeredForCreate = new ArrayList<>();
        registeredForUpdate = new ArrayList<>();
        registeredForDelete = new ArrayList<>();
    }

    public void registerNew(Task task) {
        registeredForCreate.add(task);
    }

    public void registerDirty(Task task) {
        registeredForUpdate.add(task);
    }

    public void registerDeleted(Task task) {
        if (registeredForCreate.contains(task)) registeredForCreate.remove(task);
        else registeredForDelete.add(task);
    }

    public void commit() {
        for (Task task : registeredForCreate) taskDao.create(task);
        for (Task task : registeredForUpdate) taskDao.update(task);
        for (Task task : registeredForDelete) taskDao.delete(task);
        registeredForCreate.clear();
        registeredForUpdate.clear();
        registeredForDelete.clear();
    }
}
