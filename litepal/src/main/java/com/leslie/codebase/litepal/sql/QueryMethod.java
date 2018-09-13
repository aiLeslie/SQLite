package com.leslie.codebase.litepal.sql;

import android.database.Cursor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class QueryMethod {
    // 装载Cursor获得数据的方法
    private static Map<String, Method> methods = new HashMap<>();

    static {
        // 获取Cursor的Class对象
        Class<?> classType = Cursor.class;
        String className = null;
        String methodName = null;
        for (int i = 0; i < SqlUtil.sql_type.length; i++) {
            try {
                // 注入方法
                className = SqlUtil.java_types[i].substring(SqlUtil.java_types[i].lastIndexOf(".") + 1);

                if (className.equals("Integer")){
                    methods.put(SqlUtil.sql_type[i], classType.getMethod("getInt", new Class[]{int.class}));
                    continue;
                } else if (className.equals("Boolean")) {
                    methods.put(SqlUtil.sql_type[i], classType.getMethod("getBlob", new Class[]{int.class}));
                    continue;
                }
                methodName = "get" + className;
                methods.put(SqlUtil.sql_type[i], classType.getMethod(methodName, new Class[]{int.class}));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();

            }
        }
    }



    /**
     * 根据列获取数据
     * @param cursor
     * @param type
     * @param column
     * @return
     */
    public static String get(Cursor cursor, String type, String column) {
        int index = cursor.getColumnIndex(column);
        return get(cursor, type, index);

    }

    /**
     * 根据索引获取数据
     * @param cursor
     * @param type
     * @param index
     * @return
     */
    public static String get(Cursor cursor, String type, int index) {

        if (methods.containsKey(type)) {
            try {
                Method method = methods.get(type);
                Object obj = method.invoke(cursor, new Object[]{index});
                int i = -1;
                if ((i = contains(type)) != -1) {
                    Class<?> classType = Class.forName(SqlUtil.java_types[i]);
                    if (Boolean.class.equals(classType)) {
                        return String.valueOf((byte)obj);
                    }
                    return String.valueOf(classType.cast(obj));
                }
                return String.valueOf(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 集合中是否包含该数据类型
     * @param type
     * @return
     */
    private static int contains(String type) {
        for (int i = 0; i < SqlUtil.java_types.length; i++) {
            if (SqlUtil.java_types[i].equals(type)) {
                return i;
            }
        }

        for (int i = 0; i < SqlUtil.sql_type.length; i++) {
            if (SqlUtil.sql_type[i].equals(type)) {
                return i;
            }
        }

        return -1;
    }




}
