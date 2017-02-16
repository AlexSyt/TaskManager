package com.example.alex.taskmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alex.taskmanager.db.TaskContract;
import com.example.alex.taskmanager.db.TaskDbHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TaskDbHelper dbHelper;
    private ListView taskListView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskListView = (ListView) findViewById(R.id.lv_tasks);
        dbHelper = new TaskDbHelper(this);

        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                final EditText etTask = new EditText(this);

                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("New Task")
                        .setMessage("What do you want to do?")
                        .setView(etTask)
                        .setPositiveButton("Add", (dialog, id) -> {
                            String task = String.valueOf(etTask.getText());
                            positiveButtonOnClick(task);
                            imm.hideSoftInputFromWindow(etTask.getWindowToken(), 0);
                        })
                        .setNegativeButton("Cancel", (dialog, id) ->
                                imm.hideSoftInputFromWindow(etTask.getWindowToken(), 0))
                        .create();
                alertDialog.show();

                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void positiveButtonOnClick(String task) {
        if (task.replaceAll(" ", "").length() > 0) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TaskContract.TaskEntry.TASK_COL_TITLE, task);
            db.insertWithOnConflict(TaskContract.TaskEntry.TABLE, null,
                    contentValues, SQLiteDatabase.CONFLICT_REPLACE);
            db.close();
            updateUI();
        }
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.tv_task_title);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE, TaskContract.TaskEntry.TASK_COL_TITLE + " = ?",
                new String[]{task});
        db.close();
        updateUI();
    }

    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.TASK_COL_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int ind = cursor.getColumnIndex(TaskContract.TaskEntry.TASK_COL_TITLE);
            taskList.add(cursor.getString(ind));
        }

        if (adapter == null) {
            adapter = new ArrayAdapter<>(this, R.layout.lv_tasks_item,
                    R.id.tv_task_title, taskList);
            taskListView.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(taskList);
            adapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }
}
