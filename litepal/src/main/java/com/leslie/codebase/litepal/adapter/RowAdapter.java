package com.leslie.codebase.litepal.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leslie.codebase.litepal.R;

import java.util.List;
import java.util.Map;

public class RowAdapter extends RecyclerView.Adapter<RowAdapter.RecyclerVHolder> {
    private String[] columns;
    private List<Map<String, String>> values;
    private Context mContext;

    public RowAdapter(String[] columns) {
        this.columns = columns;
    }

    public RowAdapter setColumns(String[] columns) {
        this.columns = columns;
        return this;
    }

    @NonNull
    @Override
    public RecyclerVHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        return new RecyclerVHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle_item_view, viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerVHolder recyclerVHolder, int i) {
        initRecyclerView(recyclerVHolder.recyclerView, i);
    }


    private void initRecyclerView(RecyclerView recyclerView, int i ) {
        recyclerView.setAdapter(new RecyclerAdapter(columns, values.get(i)));

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);

        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public static class RecyclerVHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        public RecyclerVHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
        }
    }


}
