package com.example.shuvam.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.renderscript.Short3;


public class TaskOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "tasks_db";
    public static final int VERSION = 1;


    private static TaskOpenHelper instance;

    public static TaskOpenHelper getInstance(Context context) {
        if (instance == null)
            instance = new TaskOpenHelper(context.getApplicationContext());
        return instance;
    }

    private TaskOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String taskSql = "CREATE TABLE " + Contract.todo.TABLE_NAME + " ( " + Contract.todo.COLUMN_TASKNAME + " TEXT , " + Contract.todo.COLUMN_SUMMARY + " TEXT , " + Contract.todo.COLUMN_date + " TEXT, " + Contract.todo.COLUMN_time + " TEXT, " + Contract.todo.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT )";
        sqLiteDatabase.execSQL(taskSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

