package com.example.alex.taskmanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(Context context) {
        super(context, DbSchema.DB_NAME, null, DbSchema.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableTask = "CREATE TABLE " + DbSchema.TaskEntry.TABLE + " ( " +
                DbSchema.TaskEntry._ID + " TEXT NOT NULL PRIMARY KEY, " +
                DbSchema.TaskEntry.TITLE + " TEXT NOT NULL, " +
                DbSchema.TaskEntry.DATE + " TEXT NOT NULL);";

        String createTableSubtasks = "CREATE TABLE " + DbSchema.SubTaskEntry.TABLE + " ( " +
                DbSchema.SubTaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbSchema.SubTaskEntry.TITLE + " TEXT NOT NULL, " +
                DbSchema.SubTaskEntry.PARENT_ID + " TEXT NOT NULL, FOREIGN KEY (" +
                DbSchema.SubTaskEntry.PARENT_ID + ") REFERENCES " +
                DbSchema.TaskEntry.TABLE + "(" + DbSchema.TaskEntry._ID + ") ON DELETE CASCADE);";

        sqLiteDatabase.execSQL(createTableTask);
        sqLiteDatabase.execSQL(createTableSubtasks);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbSchema.TaskEntry.TABLE + ";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbSchema.SubTaskEntry.TABLE + ";");
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}
