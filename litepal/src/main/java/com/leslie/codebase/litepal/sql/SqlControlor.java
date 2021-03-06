package com.leslie.codebase.litepal.sql;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;

import org.litepal.LitePalApplication;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SqlControlor {
    private static final String TAG = "SqlControlor";
    private SQLiteDatabase mDatabase;
    private String name;
    private List<String> tables = new ArrayList<>();
    private List<Cursor> cursors = new ArrayList<>();
    private List<Map<String, String>> values = new ArrayList<>();
    private SharedPreferences typeMap;
    private String currTable;

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

        currTable = tableName;

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

    public boolean dropTable(String tableName) {
        mDatabase.execSQL(SqlUtil.dropTable(tableName));
        SharedPreferences.Editor editor = typeMap.edit();
        for (String key : typeMap.getAll().keySet()) {
            if (key.startsWith(tableName + "$"))
                editor.remove(key);
        }
        editor.apply();

        return true;
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

    public boolean insertTable(String tableName, List<String> fields) {
        String name, value;
        String separator = " = ";
        ContentValues contentValues = new ContentValues();
        for (String field : fields) {
            try {
                if (field.contains(separator)) {
                    name = field.substring(0, field.indexOf(separator));
                    value = field.substring(field.indexOf(separator) + separator.length());
                    contentValues.put(name, value);
                }

            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }


        }
        if (contentValues.size() == 0) {
            return false;
        }
        mDatabase.insert(tableName, null, contentValues);

        fetchValues(tableName);

        return true;

    }

    public boolean updateTable(String tableName, List<String> fields, String where) {
        if (TextUtils.isEmpty(where)) {
            return false;
        }

        String name, value;
        String separator = " = ";
        ContentValues contentValues = new ContentValues();
        for (String field : fields) {
            try {
                if (field.contains(separator)) {
                    name = field.substring(0, field.indexOf(separator));
                    value = field.substring(field.indexOf(separator) + separator.length());
                    contentValues.put(name, value);
                }

            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }


        }
        if (contentValues.size() == 0) {
            return false;
        }

        String symbol = null;
        String[] clauses = where.split("and");
        String[] args = new String[clauses.length];

        for (int i = 0; i < clauses.length; i++) {


            try {
                if ((symbol = SqlUtil.containSymbol(clauses[i])) != null) {
                    name = clauses[i].substring(0, clauses[i].indexOf(symbol)).trim();
                    value = clauses[i].substring(clauses[i].indexOf(symbol) + symbol.length()).trim();


                    clauses[i] = name + symbol + SqlUtil.QUESTION_MARK;
                    args[i] = value;

                }
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        StringBuilder whereClause = new StringBuilder();
        for (int i = 0; i < clauses.length; i++) {
            whereClause.append(clauses[i]);
            if (i != clauses.length - 1) {
                whereClause.append(" and ");
            }

        }


        int i = mDatabase.update(tableName, contentValues, whereClause.toString(), args);

        if (i == 0) return false;

        fetchValues(tableName);

        return true;
    }

    public boolean deleteFromTable(String tableName, List<String> fields) {
        if (tableName != null && (currTable == null || currTable.equals(tableName))) {
            StringBuilder clauses = new StringBuilder();
            List<String> args = new ArrayList();

            String clause, value;
            String symbol = null;

            for (String field : fields) {
                try {
                    if ((symbol = SqlUtil.containSymbol(field)) != null) {
                        clause = field.substring(0, field.indexOf(symbol) + symbol.length()).trim();
                        value = field.substring(field.indexOf(symbol) + symbol.length()).trim();

                        clauses.append(clause + SqlUtil.QUESTION_MARK);
                        if (fields.indexOf(field) != fields.size() - 1) {
                            clauses.append(" and ");
                        }

                        args.add(value);

                    }

                } catch (StringIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }


            }


            if (clauses.length() == 0 || args.size() == 0) {
                return false;
            }

            int row = mDatabase.delete(tableName, clauses.toString(), args.toArray(new String[args.size()]));
            if (row == 0) return false;
            else {
                fetchValues(tableName);
                return true;
            }
        } else {
            throw new RuntimeException("列表数据异常");

        }


    }

    public Map<String, String> getRow(String tableName, int index) {
        if (tableName != null && (currTable == null || currTable.equals(tableName))) {
            currTable = tableName;
            Map<String, String> map = new HashMap<>();
            map.putAll(values.get(index));
            return map;
        } else {
            throw new RuntimeException("列表数据异常");
        }
    }

    public boolean deleteARow(String tableName, int index) {
        if (tableName != null && (currTable == null || currTable.equals(tableName))) {
            Map<String, String> map = values.get(index);
            StringBuilder builder = new StringBuilder();
            List<String> args = new ArrayList<>();
            int i = 0;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (!"null".equals(entry.getValue()) && !"0".equals(entry.getValue())) {
                    if (builder.length() != 0) {
                        builder.append(" and ");
                    }

                    builder.append(entry.getKey() + " = ?");
                    args.add(entry.getValue());

                }

            }
            if (builder.length() == 0) return false;
            String whereClause = builder.toString();
            int row = mDatabase.delete(tableName, whereClause, args.toArray(new String[args.size()]));
            if (row == 0) return false;
            else {
                fetchValues(tableName);

                return true;
            }
        } else {
            throw new RuntimeException("列表数据异常");

        }


    }

    public String getType(String tableName, String field) {
        return typeMap.getString(tableName + "$" + field + "_TYPE", "null");
    }


}
