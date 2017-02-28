package com.example.alex.taskmanager.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.alex.taskmanager.Task;
import com.example.alex.taskmanager.db.DbSchema.TaskEntry;

import java.util.ArrayList;
import java.util.UUID;

@SuppressLint("NewApi")
public class TaskDao {
    private final DbHelper dbHelper;
    private final ArrayList<Task> tasks;

    public TaskDao(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
        tasks = new ArrayList<>();
    }

    public void create(Task task) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put(TaskEntry._ID, task.getId().toString());
            cv.put(TaskEntry.TITLE, task.getText());
            cv.put(TaskEntry.DATE, task.getCreatedDate());
            db.insert(TaskEntry.TABLE, null, cv);
        }
    }

    public ArrayList<Task> readAll() {
        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.query(TaskEntry.TABLE, null, null, null, null, null, null)) {
            tasks.clear();
            while (cursor.moveToNext()) {
                tasks.add(new Task(UUID.fromString(cursor.getString(0)), cursor.getString(1),
                        cursor.getString(2)));
            }
            return tasks;
        }
    }

    public void update(Task task) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put(TaskEntry.TITLE, task.getText());
            cv.put(TaskEntry.DATE, task.getCreatedDate());
            db.update(TaskEntry.TABLE, cv, TaskEntry._ID + " = ?",
                    new String[]{String.valueOf(task.getId())});
        }
    }

    public void delete(Task task) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            db.delete(TaskEntry.TABLE, TaskEntry._ID + " = ?",
                    new String[]{String.valueOf(task.getId())});
        }
    }
}
