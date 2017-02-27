package com.example.alex.taskmanager.db;

import android.provider.BaseColumns;

public class DbContract {
    public static final String DB_NAME = "TaskManagerDb";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "tasks";
        public static final String COL_TITLE = "title";
        public static final String COL_DATE = "date";
    }
}
