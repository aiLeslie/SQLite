package com.example.myprovider.sql;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.myprovider.ApplicationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;

/**
 * Created by Administrator on 2018/3/13.
 */

public class MyProvider extends ContentProvider {
    private static final String TAG = "MyProvider";
    private static final String PACKAGE_NAME = "com.example.myprovider.sql"; // 包名
    private static UriMatcher macher; // 匹配者
    private MyOpenHelper openHelper; // 自己实现SQLiteOpenHelper的子类对象
    private SQLiteDatabase database; // 数据库对象
    private HashMap<String, SQLiteDatabase> databases = new HashMap<>();
    private SharedPreferences mUriCodes;
    private OnUpdateListener updateListener;


    // 静态初始化
    static {
        // 实例化匹配者
        macher = new UriMatcher(UriMatcher.NO_MATCH);
//        macher.addURI(PACKAGE_NAME, "person", 0);


    }


    @Override
    public boolean onCreate() {
        ApplicationUtils.mContentProvider = this;

        init();

        return true;
    }

    private void init() {
        mUriCodes = getContext().getSharedPreferences("UriMatcher", Context.MODE_PRIVATE);
        initUriMatcher();
        initDatabases();
    }

    private void initUriMatcher() {

        for (String action : mUriCodes.getAll().keySet()) {
            if (action.endsWith("_version")) continue;

            macher.addURI(PACKAGE_NAME, action, mUriCodes.getInt(action, -1));

        }
    }

    private void initDatabases() {
        for (String action : mUriCodes.getAll().keySet()) {
            if (action.endsWith("_version")) continue;

            databases.put(action, new MyOpenHelper(getContext(), action, null, mUriCodes.getInt(action + "_version", -1)).getWritableDatabase());

        }

    }

    public SQLiteDatabase newDatabase(String name, int version) {
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getContext(), "数据库名字不能为空", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (mUriCodes.contains(name)) {
            Toast.makeText(getContext(), "数据库已存在, 不需要重新创建!", Toast.LENGTH_SHORT).show();
            return null;
        }
        // 实例化 Helper对象
        openHelper = new MyOpenHelper(getContext(), name, null, version);
        // 得到数据库的对象
        database = openHelper.getWritableDatabase();

        mUriCodes.edit().putInt(name, mUriCodes.getAll().size()).putInt(name + "_version", version).apply();

        addUriToMatcher(name);

        databases.put(name, database);

        Toast.makeText(getContext(), "成功创建 \"" + name + "\"数据库", Toast.LENGTH_SHORT).show();

        return database;
    }

    public SQLiteDatabase deleteDatabase(String name) {
        if (databases.containsKey(name)) {

            SQLiteDatabase removeBase = databases.remove(name);
            removeBase.execSQL("drop database " + name);
            mUriCodes.edit().remove(name).remove(name + "_version").apply();
            update();
            return databases.remove(name);

        } else {
            return null;
        }
    }

    private void addUriToMatcher(String name) {
        macher.addURI(PACKAGE_NAME, name, mUriCodes.getInt(name, -1));
    }

//    private void removeUriForMacher(String name) {
//
//    }

    public HashMap<String, SQLiteDatabase> getDatabases() {
        return databases;
    }

    public void queryTable(String dbName,QueryTableListener listener) {
        if (databases.containsKey(dbName)) {
            if (listener != null) {
                listener.onQuery(databases.get(dbName));
            }
        }
    }


//    public MyProvider insertData(String name) {
//        // 编辑数据
//        ContentValues values = new ContentValues();
//        values.put("name", "李玮斌");
//        values.put("age", 5);
//
//        // 插入数据
//        database.insert("person", null, values);
//    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        switch (macher.match(uri)) {
            case 0:
                return database.query("person", null, null, null, null, null, null);
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        switch (macher.match(uri)) {
//            case TABBLE1_DIR:
//                break;
//            case TABBLE1_ITEM:
//                break;
//            case TABBLE2_DIR:
//                break;
//            case TABBLE2_ITEM:
//                break;

//            case 0:
//                return database.query("person", null, null, null, null, null, null);
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String
            s, @Nullable String[] strings) {
        return 0;
    }


    public interface QueryTableListener {
        void onQuery(SQLiteDatabase database);
    }



    public interface OnUpdateListener{
        void onUpdate();
    }

    public void setUpdateListener(OnUpdateListener onUpdateListener) {
        this.updateListener = onUpdateListener;
    }

    private void update() {
        if (updateListener != null) updateListener.onUpdate();
    }
}
