package com.example.administrator.contentresolvertest.recycle.holder;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.contentresolvertest.R;

public class PictureViewHolder extends RecyclerView.ViewHolder{
    private TextView textView;
    private ImageView imageView;

    public PictureViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.picInfo);
        imageView = itemView.findViewById(R.id.pic);
        imageView.setImageResource(R.drawable.leslie);
    }


    public ImageView getImageView() {
        return imageView;
    }


    public TextView getTextView() {

        return textView;
    }
}
