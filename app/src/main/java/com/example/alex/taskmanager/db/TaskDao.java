package com.example.alex.taskmanager.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.alex.taskmanager.Task;

import java.util.ArrayList;

public class TaskDao {
    private TaskDbHelper dbHelper;
    private ArrayList<Task> tasks;
    private SQLiteDatabase db;

    public TaskDao(TaskDbHelper dbHelper) {
        this.dbHelper = dbHelper;
        tasks = new ArrayList<>();
    }

    public void createTask(String task) {
        db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskContract.TaskEntry.TASK_COL_TITLE, task);
        db.insertWithOnConflict(TaskContract.TaskEntry.TABLE, null,
                contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public ArrayList<Task> readAllTasks() {
        tasks.clear();
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.TASK_COL_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int taskIdInd = cursor.getColumnIndex(TaskContract.TaskEntry._ID);
            int taskTextInd = cursor.getColumnIndex(TaskContract.TaskEntry.TASK_COL_TITLE);
            tasks.add(new Task(cursor.getLong(taskIdInd), cursor.getString(taskTextInd)));
        }
        cursor.close();
        db.close();
        return tasks;
    }

    public void updateTask(Task task) {
        // in future
    }

    public void deleteTask(Task task) {
        // in future
    }
}
