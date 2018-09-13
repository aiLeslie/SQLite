package com.leslie.codebase.litepal.sql;

import android.content.ContentValues;
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
                    map.put(column, QueryMethod.get(cursor, typeMap.getString(tableName + "$" + column + "_TYPE", "char"), column));
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

        try {
            execute(SqlUtil.createTable(tableName, fields));
        } catch (SQLException e) {
            return false;
        }
        SharedPreferences.Editor editor = typeMap.edit();
        String name, type;
        String separator = " ";
        for (String field : fields) {
            name = field.substring(0, field.indexOf(separator));
            type = field.substring(field.indexOf(separator) + 1);
            editor.putString(tableName + "$" + name + "_TYPE", type);
        }
        editor.apply();

        return true;
    }

    public boolean insertTable(String tableName, String[] fields) {
        String name, value;
        String separator = " = ";
        ContentValues contentValues = new ContentValues();
        for (String field : fields) {
            try {
                name = field.substring(0, field.indexOf(separator));
                value = field.substring(field.indexOf(separator )+ separator.length());
                contentValues.put(name, value);
            }catch(StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }


        }
        if(contentValues.size() == 0) {
            return false;
        }
        mDatabase.insert(tableName, null, contentValues);
        return true;

    }

    public String getType(String tableName, String field) {
        return typeMap.getString(tableName + "$" + field + "_TYPE", "null");
    }


}
