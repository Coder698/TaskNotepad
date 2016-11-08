package com.liuguilin.tasknotepad.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/10/27.
 */

public class TaskDBHelper extends SQLiteOpenHelper {

    private static final String DBNAME="task.db";

    private static final int DATABASE_VERSION=1;	//需要更新的数据库版本

    public TaskDBHelper(Context context) {
        super(context, DBNAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "create table taskinfo(id integer primary key autoincrement," +
            "day varchar(255),time varchar(255),content varchar(255))";
        db.execSQL(sql);

    }


    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
