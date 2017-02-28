package com.example.alex.taskmanager.db;

import android.provider.BaseColumns;

public class DbSchema {
    public static final String DB_NAME = "TaskManagerDb";
    public static final int DB_VERSION = 2;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "tasks";
        public static final String TITLE = "title";
        public static final String DATE = "date";
    }

    public class SubTaskEntry implements BaseColumns {
        public static final String TABLE = "subtasks";
        public static final String TITLE = "title";
        public static final String PARENT_ID = "parentId";
    }
}
