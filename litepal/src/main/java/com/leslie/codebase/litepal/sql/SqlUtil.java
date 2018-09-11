package com.leslie.codebase.litepal.sql;

/**
 * 本类用来快捷创建SQL语句
 */
public class SqlUtil {
    public static String[] java_types = {
            "string", "int", "short", "long", "int", "float", "double", "blob",
    };

    public static String[] sql_type = {
            "char", "int", "short", "long", "int", "float", "double", "blob",
    };

    private static StringBuilder builder = new StringBuilder();

//    /**
//     * 新建数据库
//     * create database dbName
//     *
//     * @param dbName
//     * @return
//     */
//    private static String createDatabase(String dbName) {
//        builder.setLength(0);
//
//        builder.append("create database ");
//        builder.append(dbName);
//        builder.append(";");
//
//        return builder.toString();
//
//    }
//
//    /**
//     * 删除数据库
//     * drop database dbName
//     *
//     * @param dbName
//     * @return
//     */
//    private static String dropDatabase(String dbName) {
//        builder.setLength(0);
//
//        builder.append("drop database ");
//        builder.append(dbName);
//        builder.append(";");
//
//        return builder.toString();
//
//    }

    /**
     * 新建表格
     * create table tableName(col1 type1 [not null] [primary key],col2 type2 [not null],..)
     *
     * @param tableName
     * @param fields
     * @return
     */
    public static String createTable(String tableName, String[] fields) {
        if (fields == null) {
            throw new IllegalArgumentException("fields == null ! ");
        }

        builder.setLength(0);

        builder.append("create table ");

        builder.append("if not exists ");

        builder.append(tableName);


        builder.append("( ");

        for (int i = 0; i < fields.length; i++) {

            builder.append(fields[i]);

            if (i != fields.length - 1) builder.append(", ");
        }

        builder.append(" );");

        return builder.toString();

    }

    /**
     * 删除表格
     * drop table tabname
     *
     * @param tableName
     * @return
     */
    public static String dropTable(String tableName) {
        builder.setLength(0);

        builder.append("drop table ");

        builder.append(tableName);

        builder.append(";");

        return builder.toString();
    }

    /**
     * 在表中插入一行数据
     * insert into table1(field1,field2) values(value1,value2)
     *
     * @param tableName
     * @param fields
     * @param args
     * @return
     */
    public static String insertARow(String tableName, String[] fields, String[] args) {


        if (fields == null || args == null) {

            throw new IllegalArgumentException("fields == null or args == null ! ");

        } else if (fields.length != args.length) {

            throw new IllegalArgumentException("fields.length != args.length");

        }

        builder.setLength(0);

        builder.append("insert into ");

        builder.append(tableName);

        builder.append("( ");

        for (int i = 0; i < fields.length; i++) {
            builder.append(fields[i]);

            if (i != fields.length - 1) builder.append(", ");
        }

        builder.append(" ) values ( ");

        for (int i = 0; i < args.length; i++) {
            builder.append(args[i]);

            if (i != args.length - 1) builder.append(", ");
        }

        builder.append(" );");

        return builder.toString();
    }

    /**
     * 在表中删除一行或多行数据
     * delete from table1 where 范围
     *
     * @param tableName
     * @param where
     * @return
     */
    public static String deleteRows(String tableName, String where) {
        if (where == null) {
            throw new IllegalArgumentException("where == null ! ");
        }

        builder.setLength(0);

        builder.append("delete from ");

        builder.append(tableName);

        builder.append(" ");

        if (!where.startsWith("where")) {
            builder.append("where ");
        }

        builder.append(where);

        builder.append(";");

        return builder.toString();
    }

    /**
     * 更新表中一行或多行数据
     * update table1 set field1=value1 where 范围
     *
     * @param tableName
     * @param fields
     * @param where
     * @return
     */
    public static String updateRows(String tableName, String[] fields, String where) {
        if (fields == null) {

            throw new IllegalArgumentException("fields == null ! ");

        } else if (where == null) {

            throw new IllegalArgumentException("where == null ! ");

        }

        builder.setLength(0);

        builder.append("update ");

        builder.append(tableName);

        builder.append(" set ");


        for (int i = 0; i < fields.length; i++) {
            builder.append(fields[i]);

            if (i != fields.length - 1) builder.append(", ");
        }

        builder.append(" ");

        if (!where.startsWith("where")) {
            builder.append("where ");
        }

        builder.append(where);

        builder.append(";");

        return builder.toString();

    }


//    private String parseString(String type, Object obj) {
//        switch (type) {
//            case "string":
//
//            case "char":
//
//                return (String) obj;
//
//            case "int":
//
//                return String.valueOf();
//        }
//    }

}
