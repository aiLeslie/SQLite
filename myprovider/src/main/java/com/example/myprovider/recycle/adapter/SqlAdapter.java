package com.example.myprovider.recycle.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;


import com.example.myprovider.recycle.IAdapter;
import com.example.myprovider.recycle.holder.SqlViewHolder;

import java.util.HashMap;
import java.util.List;

public class SqlAdapter extends IAdapter<SqlViewHolder> {


    public SqlAdapter(Context context, int layoutId, List<HashMap<String, Object>> data) {
        super(context, layoutId, data);
    }

    @Override
    public SqlViewHolder getViewHolder(View view) {
        return new SqlViewHolder(view);
    }
}
