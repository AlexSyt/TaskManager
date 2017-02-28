package com.example.alex.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.alex.taskmanager.db.DbSchema;

public class SubitemsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lv);

        Intent intent = getIntent();
        String parentId = intent.getStringExtra(DbSchema.SubTaskEntry.PARENT_ID);
    }
}
