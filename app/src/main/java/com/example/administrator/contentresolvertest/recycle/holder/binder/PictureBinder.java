package com.example.administrator.contentresolvertest.recycle.holder.binder;

import com.example.administrator.contentresolvertest.recycle.IAdapter;
import com.example.administrator.contentresolvertest.recycle.holder.PictureViewHolder;

import java.util.HashMap;
import java.util.Map;

public class PictureBinder implements IAdapter.ViewBinder<PictureViewHolder> {


    @Override
    public void onBind(PictureViewHolder pictureViewHolder, HashMap<String, Object> map) {

        for (Map.Entry<String, Object> action : map.entrySet()) {
            pictureViewHolder.getTextView().setText((String) (action.getKey() + ": " + action.getValue()));
        }
    }
}
