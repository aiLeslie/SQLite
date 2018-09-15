package com.leslie.codebase.litepal.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;


import java.util.Map;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.TextViewHolder> {
    private String[] columns;
    private Map<String, String> value;

    public RecyclerAdapter(String[] columns, Map<String, String> obj) {
        this.columns = columns;
        this.value = obj;
    }

    @NonNull
    @Override
    public TextViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TextViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(android.R.layout.simple_expandable_list_item_2, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TextViewHolder textViewHolder, int i) {

        String column = columns[i];

        textViewHolder.textView1.setText(columns[i]);
        textViewHolder.textView2.setText(value.get(column));
    }

    @Override
    public int getItemCount() {
        if (columns == null) return 0;
        return columns.length;
    }

    public static class TextViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView textView2;

        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView1 = itemView.findViewById(android.R.id.text1);
            textView2 = itemView.findViewById(android.R.id.text2);
        }
    }
}
