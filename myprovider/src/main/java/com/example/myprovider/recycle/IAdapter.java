package com.example.myprovider.recycle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.HashMap;
import java.util.List;

public abstract class IAdapter<H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<H>  {
    private Context context; // 上下文对象
    private int layoutId; // 布局id
    private List<HashMap<String, Object>> data; // 数据列表
    private ViewBinder binder; // 装订器对象


    public IAdapter(Context context, int layoutId, List<HashMap<String, Object>> data) {
        this.context = context;
        this.layoutId = layoutId;
        this.data = data;
    }


    /**
     * 创建ViewHolder对象回调方法
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public H onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);

        // 创建ViewHolder对象
        return (H) getViewHolder(itemView);
    }

    public abstract H getViewHolder(View view);

    /**
     * 设置装订器
     *
     * @param binder
     */
    public void setBinder(ViewBinder binder) {
        this.binder = binder;
    }

    /**
     * 装订ViewHolder回调
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(H holder, int position) {
        HashMap<String, Object> map = data.get(position);
        if (binder != null) binder.onBind(holder, map);
    }


    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }



    /**
     * 装订器接口
     *
     * @param <H>
     */
    public interface ViewBinder<H extends RecyclerView.ViewHolder> {
        public void onBind(H h, HashMap<String, Object> map);
    }
}
