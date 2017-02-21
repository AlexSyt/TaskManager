package com.example.alex.taskmanager.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.alex.taskmanager.Task;

import java.util.ArrayList;

public class TaskDao {
    private final TaskDbHelper dbHelper;
    private final ArrayList<Task> tasks;
    private SQLiteDatabase db;

    public TaskDao(TaskDbHelper dbHelper) {
        this.dbHelper = dbHelper;
        tasks = new ArrayList<>();
    }

    public void create(Task task) {
        db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskContract.TaskEntry.TASK_COL_TITLE, task.getText());
        contentValues.put(TaskContract.TaskEntry.TASK_COL_DATE, task.getCreatedDate());
        db.insertWithOnConflict(TaskContract.TaskEntry.TABLE, null,
                contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public ArrayList<Task> readAll() {
        tasks.clear();
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.TASK_COL_TITLE,
                        TaskContract.TaskEntry.TASK_COL_DATE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int taskIdInd = cursor.getColumnIndex(TaskContract.TaskEntry._ID);
            int taskTextInd = cursor.getColumnIndex(TaskContract.TaskEntry.TASK_COL_TITLE);
            int taskDateInd = cursor.getColumnIndex(TaskContract.TaskEntry.TASK_COL_DATE);
            tasks.add(new Task(cursor.getLong(taskIdInd), cursor.getString(taskTextInd),
                    cursor.getString(taskDateInd)));
        }
        cursor.close();
        db.close();
        return tasks;
    }

    public void update(Task task) {
        db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskContract.TaskEntry.TASK_COL_TITLE, task.getText());
        contentValues.put(TaskContract.TaskEntry.TASK_COL_DATE, task.getCreatedDate());
        db.update(TaskContract.TaskEntry.TABLE, contentValues, TaskContract.TaskEntry._ID + " = ?",
                new String[]{String.valueOf(task.getId())});
        db.close();
    }

    public void delete(Task task) {
        db = dbHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE, TaskContract.TaskEntry._ID + " = ?",
                new String[]{String.valueOf(task.getId())});
        db.close();
    }
}
