package com.example.administrator.contentresolvertest.recycle.adapter;

import android.content.Context;
import android.view.View;

import com.example.administrator.contentresolvertest.recycle.IAdapter;
import com.example.administrator.contentresolvertest.recycle.holder.PictureViewHolder;

import java.util.HashMap;
import java.util.List;

public class PicAdapter extends IAdapter<PictureViewHolder>{
    public PicAdapter(Context context, int layoutId, List<HashMap<String, Object>> data) {
        super(context, layoutId, data);
    }

    @Override
    public PictureViewHolder getViewHolder(View view) {
        return new PictureViewHolder(view);
    }
}
