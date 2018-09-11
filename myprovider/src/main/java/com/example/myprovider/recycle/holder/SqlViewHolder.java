package com.example.myprovider.recycle.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.myprovider.R;

public class SqlViewHolder extends RecyclerView.ViewHolder {
    public RecyclerView recyclerView;


    public SqlViewHolder(View itemView) {
        super(itemView);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);
    }

}
