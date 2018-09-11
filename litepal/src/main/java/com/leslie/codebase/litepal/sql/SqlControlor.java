package com.leslie.codebase.litepal.sql;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.litepal.LitePalApplication;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlControlor {
    private static final String TAG = "SqlControlor";
    private SQLiteDatabase mDatabase;
    private String name;
    private List<String> tables = new ArrayList<>();
    private List<Cursor> cursors = new ArrayList<>();
    private List<Map<String, String>> values = new ArrayList<>();
    private SharedPreferences typeMap;

    {
        typeMap = LitePalApplication.getContext().getSharedPreferences("TYPES", Context.MODE_PRIVATE);
    }

    public SqlControlor() {
        this.mDatabase = Connector.getDatabase();

    }

    public SqlControlor(SQLiteDatabase mDatabase) {
        this.mDatabase = mDatabase;
    }


    public SharedPreferences getTypeMap() {
        return typeMap;
    }

    public String databaseName() {
        if (name == null) {
            name = mDatabase.getPath().substring(mDatabase.getPath().lastIndexOf("/") + 1);
        }

        return name;
    }

    public void execute(String sql) {
        mDatabase.execSQL(sql);
    }

    public List updateTables() {
        tables.clear();

        Cursor cursor = mDatabase.rawQuery("select name from sqlite_master where type='table' order by name", null);

        while (cursor.moveToNext()) {
            tables.add(cursor.getString(0));
        }
        return tables;
    }

    public String[] columns(String tableName) {
        Cursor cursor = mDatabase.query(tableName, null, null, null, null, null, null);

        return cursor.getColumnNames();
    }

    public List fetchValues(String tableName) {

        Cursor cursor = mDatabase.query(tableName, null, null, null, null, null, null);
        if (cursor != null) {

            clearValues();

            String[] columns = columns(tableName);

            Map<String, String> map = null;

            while (cursor.moveToNext()) {
                map = new HashMap<>();

                for (String column : columns) {
                    map.put(column, String.valueOf(QueryMethod.get(cursor, typeMap.getString(tableName + "$" + column + "_TYPE", "char"), column)));
                }

                values.add(map);

            }
        }
        return values;
    }

    private void clearValues() {
        for (Map<String, String> value : values) {
            value.clear();
        }
        values.clear();
    }

    public boolean createTable(String tableName, String[] fields) {
        if (updateTables().contains(tableName)) {
            return false;
        }
        for (int i = 0; i < fields.length - 1; i++) {
            for (int j = i + 1; j < fields.length; j++) {
                if (fields[i].equals(fields[j])) {
                    return false;
                }
            }
        }

        try{
            execute(SqlUtil.createTable(tableName, fields));
        }catch (SQLException e) {
            return false;
        }
        return true;
    }


}
