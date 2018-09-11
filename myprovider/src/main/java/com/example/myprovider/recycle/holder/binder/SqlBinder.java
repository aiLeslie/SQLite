package com.example.myprovider.recycle.holder.binder;

import android.support.v7.widget.LinearLayoutManager;


import com.example.myprovider.R;
import com.example.myprovider.recycle.IAdapter;
import com.example.myprovider.recycle.holder.SqlViewHolder;

import java.util.HashMap;

public class SqlBinder implements IAdapter.ViewBinder<SqlViewHolder> {


    @Override
    public void onBind(SqlViewHolder sqlHolder, HashMap<String, Object> map) {
//        initRecycleView();
    }

    /**
     * 初始化recycleView
     */
//    private void initRecycleView() {
//
//
//
//        IAdapter<PictureViewHolder> iAdapter = new PicAdapter(this, R.layout.pic_item, contactMapList);
//
//        iAdapter.setBinder(new PictureBinder());
//
//        recyclerView.setAdapter(iAdapter);
//
//        // 线性布局管理器
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(linearLayoutManager); // 设置管理器
//
//        // 瀑布流布局管理器
////        recyclerView.setLayoutManager(new StaggeredGridLayoutManager( 2, StaggeredGridLayoutManager.HORIZONTAL)); // 设置管理器
//
//
//    }
}
