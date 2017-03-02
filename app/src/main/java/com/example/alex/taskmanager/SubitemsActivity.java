package com.example.alex.taskmanager;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
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

import com.example.alex.taskmanager.db.DbHelper;
import com.example.alex.taskmanager.db.DbSchema.SubTaskEntry;
import com.example.alex.taskmanager.db.DbSchema.TaskEntry;

public class SubitemsActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    private SQLiteDatabase db;
    private SimpleCursorAdapter scAdapter;
    private String parentId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lv);

        Intent intent = getIntent();
        parentId = intent.getStringExtra(SubTaskEntry.PARENT_ID);
        String parentTitle = intent.getStringExtra(TaskEntry.TITLE);

        getSupportActionBar().setTitle(parentTitle);

        DbHelper dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();

        String[] from = new String[]{SubTaskEntry.TITLE};
        int[] to = new int[]{R.id.tv_task_subitem_title};

        scAdapter = new SimpleCursorAdapter(this, R.layout.lv_task_subitem, null, from, to, 0);
        ListView lvSubitems = (ListView) findViewById(R.id.lv_tasks);
        registerForContextMenu(lvSubitems);
        lvSubitems.setAdapter(scAdapter);

        getSupportLoaderManager().restartLoader(0, null, this);
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
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
                        .setTitle("New Sub-item")
                        .setView(etTask)
                        .setPositiveButton("Add", (dialog, id) -> {
                            String taskText = String.valueOf(etTask.getText());
                            if (taskText.replaceAll(" ", "").length() > 0) {
                                ContentValues cv = new ContentValues();
                                cv.put(SubTaskEntry.TITLE, taskText);
                                cv.put(SubTaskEntry.PARENT_ID, parentId);
                                db.insert(SubTaskEntry.TABLE, null, cv);
                                getSupportLoaderManager().getLoader(0).forceLoad();
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

    private static final int CM_UPDATE_ID = 1;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_UPDATE_ID, 0, "Update");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo acmi =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case CM_UPDATE_ID:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                final EditText etTask = new EditText(this);

                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("Update Sub-task")
                        .setView(etTask)
                        .setPositiveButton("Update", (dialog, id) -> {
                            String taskText = String.valueOf(etTask.getText());
                            if (taskText.replaceAll(" ", "").length() > 0) {
                                ContentValues cv = new ContentValues();
                                cv.put(SubTaskEntry.TITLE, taskText);
                                db.update(SubTaskEntry.TABLE, cv, TaskEntry._ID + " = ?",
                                        new String[]{String.valueOf(acmi.id)});
                                getSupportLoaderManager().getLoader(0).forceLoad();
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
                return super.onContextItemSelected(item);
        }
    }

    public void btnDoneOnClick(View view) {
        View parent = (View) view.getParent();
        ListView listView = (ListView) parent.getParent();
        final int position = listView.getPositionForView(parent);
        Cursor c = scAdapter.getCursor();
        c.moveToPosition(position);
        db.delete(SubTaskEntry.TABLE, SubTaskEntry._ID + " = " + c.getLong(0), null);
        getSupportLoaderManager().getLoader(0).forceLoad();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MyCursorLoader(this, db, parentId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        scAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    static class MyCursorLoader extends CursorLoader {
        private final SQLiteDatabase db;
        private final String parentId;

        public MyCursorLoader(Context context, SQLiteDatabase db, String parentId) {
            super(context);
            this.db = db;
            this.parentId = parentId;
        }

        @Override
        public Cursor loadInBackground() {
            return db.query(SubTaskEntry.TABLE, null, SubTaskEntry.PARENT_ID + " = ?",
                    new String[]{parentId}, null, null, null);
        }
    }
}
