package com.example.myprovider.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import javax.security.auth.login.LoginException;

public class MyOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "MyOpenHelper";
    private static final String CREATE_TABLE_PERSON = "create table person (id integer primary key autoincrement,name text,age integer)";
    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 第一次创建数据库调用该方法
     * (如果已存在该数据库就不调用该方法)
     * @param database
     */
    @Override
    public void onCreate(SQLiteDatabase database) {
        // 执行sql语句(创建表)
        database.execSQL(CREATE_TABLE_PERSON);
        Log.i(TAG, "Greate succeed");
    }

    /**
     * 更新版本号调用该方法
     * @param database
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade: ");
    }



}
