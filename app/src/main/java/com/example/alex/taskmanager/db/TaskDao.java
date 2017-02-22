package com.example.alex.taskmanager.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.alex.taskmanager.Task;

import java.util.ArrayList;

public class TaskDao {
    private final TaskDbHelper dbHelper;
    private final ArrayList<Task> tasks;

    public TaskDao(TaskDbHelper dbHelper) {
        this.dbHelper = dbHelper;
        tasks = new ArrayList<>();
    }

    public void create(Task task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskContract.TaskEntry.TASK_COL_TITLE, task.getText());
        contentValues.put(TaskContract.TaskEntry.TASK_COL_DATE, task.getCreatedDate());
        db.insertWithOnConflict(TaskContract.TaskEntry.TABLE, null,
                contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public ArrayList<Task> readAll() {
        tasks.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.TASK_COL_TITLE,
                        TaskContract.TaskEntry.TASK_COL_DATE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            tasks.add(new Task(cursor.getLong(0), cursor.getString(1), cursor.getString(2)));
        }
        cursor.close();
        db.close();
        return tasks;
    }

    public void update(Task task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskContract.TaskEntry.TASK_COL_TITLE, task.getText());
        contentValues.put(TaskContract.TaskEntry.TASK_COL_DATE, task.getCreatedDate());
        db.update(TaskContract.TaskEntry.TABLE, contentValues, TaskContract.TaskEntry._ID + " = ?",
                new String[]{String.valueOf(task.getId())});
        db.close();
    }

    public void delete(Task task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE, TaskContract.TaskEntry._ID + " = ?",
                new String[]{String.valueOf(task.getId())});
        db.close();
    }
}
