package com.example.alex.taskmanager;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.alex.taskmanager.db.TaskDao;
import com.example.alex.taskmanager.db.TaskDbHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final int CM_UPDATE_ID = 1;
    private static final int CM_DELETE_ID = 2;
    private ListView taskListView;
    private TaskDao taskDao;
    private SimpleAdapter adapter;
    private ArrayList<Task> tasks;
    private ArrayList<Map<String, String>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskListView = (ListView) findViewById(R.id.lv_tasks);
        registerForContextMenu(taskListView);
        taskDao = new TaskDao(new TaskDbHelper(this));
        data = new ArrayList<>();

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
                            if (task.replaceAll(" ", "").length() > 0) {
                                taskDao.createTask(task, getDate());
                                updateUI();
                            }
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        String name = tasks.get(acmi.position).getText();
        menu.setHeaderTitle(name);
        menu.add(0, CM_UPDATE_ID, 0, "Update");
        menu.add(0, CM_DELETE_ID, 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo acmi =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case CM_UPDATE_ID:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                Task task = tasks.get(acmi.position);
                final EditText etTask = new EditText(this);
                etTask.setText(task.getText());
                etTask.setSelection(etTask.getText().length());

                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("Update Task")
                        .setView(etTask)
                        .setPositiveButton("Update", (dialog, id) -> {
                            String taskText = String.valueOf(etTask.getText());
                            if (taskText.replaceAll(" ", "").length() > 0
                                    && !taskText.equals(task.getText())) {
                                task.setText(taskText);
                                task.setCreatedDate(getDate());
                                taskDao.updateTask(task);
                                updateUI();
                            }
                            imm.hideSoftInputFromWindow(etTask.getWindowToken(), 0);
                        })
                        .setNegativeButton("Cancel", (dialog, id) ->
                                imm.hideSoftInputFromWindow(etTask.getWindowToken(), 0))
                        .create();
                alertDialog.show();

                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                return true;

            case CM_DELETE_ID:
                taskDao.deleteTask(tasks.get(acmi.position));
                updateUI();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM HH:mm", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    private void updateUI() {
        tasks = taskDao.readAllTasks();
        ArrayList<String> taskTextList = new ArrayList<>();
        ArrayList<String> taskDateList = new ArrayList<>();
        for (Task task : tasks) {
            taskTextList.add(task.getText());
            taskDateList.add(task.getCreatedDate().replace(" ", "\n"));
        }

        final String ATTRIBUTE_NAME_TEXT = "text";
        final String ATTRIBUTE_NAME_DATE = "date";

        data.clear();
        Map<String, String> map;
        for (int i = 0; i < tasks.size(); i++) {
            map = new HashMap<>();
            map.put(ATTRIBUTE_NAME_TEXT, taskTextList.get(i));
            map.put(ATTRIBUTE_NAME_DATE, taskDateList.get(i));
            data.add(map);
        }

        String[] from = {ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_DATE};
        int[] to = {R.id.tv_task_title, R.id.tv_task_date};

        if (adapter == null) {
            adapter = new SimpleAdapter(this, data, R.layout.lv_tasks_item, from, to);
            taskListView.setAdapter(adapter);
        } else adapter.notifyDataSetChanged();
    }
}
