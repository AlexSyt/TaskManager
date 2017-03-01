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
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.example.alex.taskmanager.db.DbHelper;
import com.example.alex.taskmanager.db.DbSchema;
import com.example.alex.taskmanager.db.DbSchema.SubTaskEntry;

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
        String parentTitle = intent.getStringExtra(DbSchema.TaskEntry.TITLE);

        getSupportActionBar().setTitle(parentTitle);

        DbHelper dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();

        String[] from = new String[]{SubTaskEntry.TITLE};
        int[] to = new int[]{R.id.tv_task_subitem_title};

        scAdapter = new SimpleCursorAdapter(this, R.layout.lv_task_subitem, null, from, to, 0);
        ListView lvSubitems = (ListView) findViewById(R.id.lv_tasks);
        lvSubitems.setAdapter(scAdapter);

        getSupportLoaderManager().initLoader(0, null, this);
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
                        .setTitle("New Subitem")
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
        private SQLiteDatabase db;
        private String parentId;

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
