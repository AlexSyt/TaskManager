package com.example.alex.taskmanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class TaskDbHelper extends SQLiteOpenHelper {
    public TaskDbHelper(Context context) {
        super(context, DbContract.DB_NAME, null, DbContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + DbContract.TaskEntry.TABLE + " ( " +
                DbContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbContract.TaskEntry.COL_TITLE + " TEXT NOT NULL, " +
                DbContract.TaskEntry.COL_DATE + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbContract.TaskEntry.TABLE);
        onCreate(sqLiteDatabase);
    }
}
