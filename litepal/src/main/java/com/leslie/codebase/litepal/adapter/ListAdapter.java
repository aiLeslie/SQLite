package com.leslie.codebase.litepal.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.TextView;

import com.leslie.codebase.litepal.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ListAdapter extends ArrayAdapter<Map<String, String>> {
    private int layoutId;
    private String[] columns;
    private List<Map<String, String>> values;

    public ListAdapter(@NonNull Context context, int resource, @NonNull List<Map<String, String>> objects) {
        super(context, resource, objects);
        layoutId = resource;
        values = objects;
    }

    public ListAdapter setColumns(String[] columns) {
        this.columns = columns;
        return this;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        initRecyclerView(recyclerView, position);

        return view;
    }

    private void initRecyclerView(RecyclerView recyclerView, int i) {
        recyclerView.setAdapter(new RecyclerAdapter(columns, values.get(i)));

//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
//        recyclerView.setLayoutManager(layoutManager);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
    }
}
