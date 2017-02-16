package com.example.alex.taskmanager.db;

import android.provider.BaseColumns;

public class TaskContract {
    public static final String DB_NAME = "TaskManagerDb";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "tasks";
        public static final String TASK_COL_TITLE = "title";
    }
}
