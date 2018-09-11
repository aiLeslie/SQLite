package com.leslie.codebase.litepal.sql;

import android.database.Cursor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class QueryMethod {
    private static Map<String, Method> methods = new HashMap<>();

    static {
        Class<?> classType = Cursor.class;
        for (String type : SqlUtil.java_types) {
            try {
                methods.put(type, classType.getMethod("get" + type.substring(0, 1).toUpperCase() + type.substring(1), new Class[]{int.class}));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public static <R> R get(Cursor cursor, String type, String column) {
        int index = cursor.getColumnIndex(column);
        return get(cursor, type, index);

    }

    public static <R> R get(Cursor cursor, String type, int index) {

        if (methods.containsKey(type)) {
            try {
                return (R) methods.get(type).invoke(cursor, new Object[]{index});
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String contains(String type) {
        for (int i = 0; i < SqlUtil.java_types.length; i++) {
            if (SqlUtil.java_types[i].equals(type)) {
                return type;
            }
        }

        for (int i = 0; i < SqlUtil.sql_type.length; i++) {
            if (SqlUtil.sql_type[i].equals(type)) {
                return SqlUtil.java_types[i];
            }
        }

        return null;
    }


}
